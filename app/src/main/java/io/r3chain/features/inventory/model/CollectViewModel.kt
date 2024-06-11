package io.r3chain.features.inventory.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.ResourcesGateway
import io.r3chain.data.repositories.WasteRepository
import io.r3chain.data.vo.FileAttachVO
import io.r3chain.data.vo.WasteVO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CollectViewModel @Inject constructor(
    private val wasteRepository: WasteRepository,
    private val resourcesGateway: ResourcesGateway
) : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные для формы.
     */
    var data: WasteVO by mutableStateOf(WasteVO())
        private set

    /**
     * Данные загружаемых файлов.
     */
    private val filesEvents = resourcesGateway.events

    /**
     * Результат обработки формы.
     */
    var doneResult: Result<WasteVO>? by mutableStateOf(null)
        private set


    init {
        viewModelScope.launch {
            filesEvents.collect { event ->
                when (event.type) {
                    ResourcesGateway.FileEventType.DONE -> updateAttach(event.file)
                    ResourcesGateway.FileEventType.REMOVE -> removeAttach(event.file)
                }
            }
        }
    }


    /**
     * Загружает файлы изображений на сервер.
     *
     * @param images Список uri загружаемых файлов.
     */
    fun uploadImages(images: List<Uri>) {
        viewModelScope.launch {
            // сформировать данные добавленных файлов
            val addedFiles = images.map {
                FileAttachVO(uri = it, isLoading = true)
            }
            // передать их в стейт формы
            changeFormData(
                value = data.copy(files = data.files + addedFiles),
                isByFile = true
            )
            // запустить их загрузку
            addedFiles.forEach {
                resourcesGateway.startUploadFile(it)
            }
        }
    }


    /**
     * Обновить данные прикрепляемого файла.
     */
    private fun updateAttach(file: FileAttachVO) {
        val newList = data.files.indexOfFirst {
            it.uri == file.uri
        }.takeIf {
            it != -1
        }?.let { index ->
            data.files.toMutableList().apply {
                set(index, file)
            }.toList()
        }
        if (newList != null) changeFormData(
            value = data.copy(files = newList),
            isByFile = true
        )
    }


    /**
     * Убрать прикрепляемый файл.
     */
    private fun removeAttach(file: FileAttachVO) {
        // список без file
        changeFormData(
            value = data.copy(files = data.files.filter { it != file }),
            isByFile = true
        )
    }


    /**
     * Отрабатывает подтверждение пользователем заполенния формы.
     */
    fun doneForm() {
        viewModelScope.launch {
            isLoading = true
            delay(500)
//            wasteRepository.addCollect()
//            isLoading = false
            // TODO: передать с успехом новые данные, которые вернул сервер
            doneResult = Result.success(data)
        }
    }


    /**
     * Изменить значение типа материала.
     */
    fun changeFormData(value: WasteVO, isByFile: Boolean = false) {
        val newData = if (!isByFile) value else value.copy(
            // первое попавшееся время
            time = value.files.find {
                it.resource?.time != null
            }?.resource?.time,
            // первая попавшаяся геолокация
            geoLatLong = value.files.find {
                it.resource?.latitude != null && it.resource?.longitude != null
            }?.resource?.let { vo ->
                vo.latitude?.let { lat ->
                    vo.longitude?.let { long ->
                        lat to long
                    }
                }
            }
        )

        // TODO: провалидировать форму (дизейблить/энейблить кнопку send)
        data = newData
    }

}