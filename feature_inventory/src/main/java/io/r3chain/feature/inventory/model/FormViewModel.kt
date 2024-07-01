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
import io.r3chain.core.data.vo.ResourceEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant

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
    var wasteData by mutableStateOf(entity)
        private set

    /**
     * Текущий документ верификации.
     */
    var verificationData: WasteDocEntity? by mutableStateOf(null)
        private set

    /**
     * Данные загружаемых файлов.
     */
    private val wasteFilesEvents = resourcesGateway.events

    /**
     * Результат обработки формы.
     */
    var doneResult: Result<WasteEntity>? by mutableStateOf(null)
        private set


    init {
        // собирать данные загрузки файлов записи мусора
        viewModelScope.launch {
            wasteFilesEvents.collect { event ->
                when (event.type) {
                    ResourcesGateway.FileEventType.DONE -> updateAttach(event.file)
                    ResourcesGateway.FileEventType.REMOVE -> removeAttach(event.file)
                }
            }
        }
    }


    /**
     * Загружает файлы изображений.
     *
     * @param uris Список uri загружаемых файлов.
     */
    fun uploadWasteResources(uris: List<Uri>) {
        viewModelScope.launch {
            // сформировать данные добавленных файлов
            val addedFiles = uris.map {
                FileAttachEntity(uri = it, isLoading = true)
            }
            // передать их в стейт формы
            changeWasteData(
                value = wasteData.copy(files = wasteData.files + addedFiles),
                isByFile = true
            )
            // запустить их загрузку на сервер
            addedFiles.forEach {
                resourcesGateway.startUploadFile(it)
            }
        }
    }


    /**
     * Загружает файлы основных фоток верефикации.
     *
     * @param doc Документ верификации, в который нужно загрузить файлы.
     * @param uris Список uri загружаемых файлов.
     */
    fun uploadVerificationResources(doc: WasteDocEntity, uris: List<Uri>) {
        viewModelScope.launch {
            // сформировать данные добавленных файлов
            val addedFiles = uris.map {
                FileAttachEntity(
                    uri = it,
                    isLoading = false,
                    // TODO: убрать этот mock
                    resource = ResourceEntity(
                        id = (0..100).random(),
                        posterLink = it.toString(),
                        latitude = 37.7749,
                        longitude = -122.4194,
                        time = Instant.now().toEpochMilli()
                    )
                )
            }
            changeVerificationData(
                value = doc.copy(files = doc.files + addedFiles)
            )
            // TODO: запустить их загрузку на сервер
        }
    }


    /**
     * Загружает файлы дополнительных фоток верефикации.
     *
     * @param doc Документ верификации, в который нужно загрузить файлы.
     * @param uris Список uri загружаемых файлов.
     */
    fun uploadVerificationResources2(doc: WasteDocEntity, uris: List<Uri>) {
        viewModelScope.launch {
            // сформировать данные добавленных файлов
            val addedFiles = uris.map {
                FileAttachEntity(
                    uri = it,
                    isLoading = false,
                    // TODO: убрать этот mock
                    resource = ResourceEntity(
                        id = (0..100).random(),
                        posterLink = it.toString(),
                        latitude = 37.7749,
                        longitude = -122.4194,
                        time = Instant.now().toEpochMilli()
                    )
                )
            }
            changeVerificationData(
                value = doc.copy(files2 = doc.files2 + addedFiles)
            )
            // TODO: запустить их загрузку на сервер
        }
    }


    /**
     * Обновить данные прикрепляемого файла.
     */
    private fun updateAttach(file: FileAttachEntity) {
        val newList = wasteData.files.indexOfFirst {
            it.uri == file.uri
        }.takeIf {
            it != -1
        }?.let { index ->
            wasteData.files.toMutableList().apply {
                set(index, file)
            }.toList()
        }
        if (newList != null) changeWasteData(
            value = wasteData.copy(files = newList),
            isByFile = true
        )
    }

    /**
     * Убрать прикрепляемый файл.
     */
    private fun removeAttach(file: FileAttachEntity) {
        // список без file
        changeWasteData(
            value = wasteData.copy(files = wasteData.files.filter { it != file }),
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
            wasteRepository.addWaste(wasteData).also {
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
    fun changeWasteData(value: WasteEntity, isByFile: Boolean = false) {
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
        wasteData = newData
    }


    /**
     * Изменить значения текущего документа верификации.
     *
     * @param value Основной объект данных.
     */
    fun changeVerificationData(value: WasteDocEntity) {
        // обновить данные формы верификации
        verificationData = value
    }

    /**
     * Создаёт шаблон для добавляемого документа по его типу.
     *
     * @param type Тип документа.
     */
    fun intentVerificationByType(type: WasteDocType) {
        changeVerificationData(
            WasteDocEntity(type = type)
        )
    }

    /**
     * Добавляет документ к соответствующему списку в данных записи мусора.
     *
     * @param doc Данные документа.
     */
    fun addVerification(doc: WasteDocEntity) {
        val newId = (wasteData.documents.maxOfOrNull { it.id } ?: 0) + 1
        changeWasteData(wasteData.copy(
            documents = wasteData.documents + listOf(doc.copy(id = newId))
        ))
    }

    /**
     * Обновляет документ в данных записи мусора.
     *
     * @param doc Данные документа.
     */
    fun updateVerification(doc: WasteDocEntity) {
        // обновить данные документа в общем списке
        val newDocs = wasteData.documents.indexOfFirst {
            it.id == doc.id
        }.takeIf {
            it != -1
        }?.let { index ->
            wasteData.documents.toMutableList().apply {
                set(index, doc)
            }.toList()
        }

        // новые документы в стейт формы
        changeWasteData(
            value = wasteData.copy(
                documents = newDocs ?: emptyList()
            ),
            isByFile = false
        )
    }

    /**
     * Удаляет документ из соответствующего списка в данных записи мусора.
     *
     * @param doc Данные документа.
     */
    fun deleteVerification(doc: WasteDocEntity) {
        changeWasteData(wasteData.copy(
            documents = wasteData.documents.filter { it.id != doc.id }
        ))
    }

}