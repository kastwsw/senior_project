package io.r3chain.feature.inventory.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.repositories.ResourcesGateway
import io.r3chain.core.data.repositories.WasteMockRepository
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteDocumentEntity
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = FormViewModel.ViewModelFactory::class)
class FormViewModel @AssistedInject constructor(
    @Assisted
    private val entity: WasteEntity,
    private val wasteRepository: WasteMockRepository,
    private val resourcesGateway: ResourcesGateway
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(entity: WasteEntity): FormViewModel
    }

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные для формы.
     */
    var data by mutableStateOf(entity)
        private set

    /**
     * Данные загружаемых файлов.
     */
    private val filesEvents = resourcesGateway.events

    /**
     * Текущий документ верификации.
     */
    var currentVerificationDoc: WasteDocumentEntity? by mutableStateOf(null)
        private set

    /**
     * Результат обработки формы.
     */
    var doneResult: Result<WasteEntity>? by mutableStateOf(null)
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
                FileAttachEntity(uri = it, isLoading = true)
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
    private fun updateAttach(file: FileAttachEntity) {
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
    private fun removeAttach(file: FileAttachEntity) {
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
            delay(300)
            // TODO: по параметрам data определить куда её передавать
            wasteRepository.addWaste(data).also {
                // TODO: передать с успехом новые данные, которые вернул сервер
                doneResult = Result.success(it)
            }
        }
    }


    /**
     * Изменить значения формы.
     *
     * @param value Основной объект данных.
     * @param isByFile True - данные из файлов имеют больший приоритет (оверрайдят данные value).
     */
    fun changeFormData(value: WasteEntity, isByFile: Boolean = false) {
        // взять время и локацию из файла, если необходимо
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

    /**
     * Создаёт шаблон для добавляемого документа по его типу.
     *
     * @param type Тип документа.
     */
    fun addDocByType(type: WasteDocType) {
        currentVerificationDoc = WasteDocumentEntity(
            type = type
        )
    }

}