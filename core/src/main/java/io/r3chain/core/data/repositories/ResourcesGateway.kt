package io.r3chain.core.data.repositories

import android.content.Context
import android.net.Uri
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import io.r3chain.core.api.infrastructure.ApiClient
import io.r3chain.core.api.models.ResourceUploadDto
import io.r3chain.core.api.models.ResourceUploadRequestDto
import io.r3chain.core.data.services.ApiService
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.ResourceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import java.time.Instant
import javax.inject.Inject

class ResourcesGateway @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val apiService: ApiService,
    private val apiClient: ApiClient
) {

    /**
     * Данные отправляемых на сервер файлов.
     */
    private val uploadMap: MutableMap<String, Pair<FileAttachEntity, Job?>> = mutableMapOf()


    private val _events = MutableSharedFlow<FileEvent>()

    /**
     * События отправляемых файлов.
     */
    val events: SharedFlow<FileEvent> = _events.asSharedFlow()


    /**
     * Запускает процесс загрузки файла.
     */
    suspend fun startUploadFile(file: FileAttachEntity) {
        withContext(Dispatchers.IO) {
            val key = makeFileKey(file)

            // если что-то по этому ключу уже загружается, то отменить
            uploadMap[key]?.second?.cancel()

            // запустить процесс отправки файла
            uploadMap[key] = file to launch {
                // подготовить данные для ответа
                var newFile = file.copy(isLoading = false)
                // запустить
//                sendFile(file.uri)
//                    .onSuccess {
//                        newFile = newFile.copy(resource = it)
//                    }
//                    .onFailure {
//                        newFile = newFile.copy(error = it.message ?: "Upload error")
//                    }
                // TODO: убрать этот mock
                delay(2000)
                newFile = newFile.copy(
                    resource = ResourceEntity(
                        id = (0..100).random(),
                        posterLink = file.uri.toString(),
                        latitude = 37.7749,
                        longitude = -122.4194,
                        time = Instant.now().toEpochMilli()
                    )
                )
                // отработать результат
                uploadMap[key] = newFile to null
                _events.emit(
                    FileEvent(file = newFile, type = FileEventType.DONE)
                )
            }
        }
    }


    /**
     * Открепляет файл.
     */
    suspend fun removeFile(file: FileAttachEntity) {
        withContext(Dispatchers.IO) {
            val key = makeFileKey(file)

            // если что-то по этому ключу уже загружается, то отменить
            uploadMap[key]?.second?.cancel()

            // убрать данные файла и отработать результат
            if (uploadMap.remove(key) != null) launch {
                _events.emit(
                    FileEvent(file = file, type = FileEventType.REMOVE)
                )
            }
        }
    }


    /**
     * Получает данные файла по ключу.
     */
    fun getActualFileData(file: FileAttachEntity) = uploadMap[makeFileKey(file)]?.first


    /**
     * Отправляет файл на сервер.
     */
    suspend fun sendFile(uri: Uri): Result<ResourceEntity> {
        // file data for request
        val fileBody = getFileMultipartBody(uri)
        // file meta for request
        val dto = getFileDtoMultipartBody(
            ResourceUploadRequestDto(listOf(ResourceUploadDto()))
        )
        // send to server
        return apiService.safeApiCall {
            apiClient
                .createService(io.r3chain.core.api.apis.ResourceApi::class.java)
                .apiV1MediaUploadPost(fileBody, dto)
        }.map {
            ResourceEntity().createByApi(
                it.resourceList!!.values.first()
            )
        }
    }


    /**
     * Создаёт мальтипарт-дата файла для отправки на сервер.
     */
    private fun getFileMultipartBody(uri: Uri) = MultipartBody.Part.createFormData(
        name = "file",
        filename = uri.lastPathSegment ?: "upload_media",
        // чтобы не создавать лишний файл
        body = object : RequestBody() {
            override fun contentType() = "image/*".toMediaTypeOrNull()

            override fun writeTo(sink: BufferedSink) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    sink.writeAll(inputStream.source())
                }
            }
        }
    )


    /**
     * Создаёт мальтипарт-дата описания файла для отправки на сервер.
     */
    private fun getFileDtoMultipartBody(data: ResourceUploadRequestDto) = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(ResourceUploadRequestDto::class.java)
        .toJson(data)
        .toRequestBody("application/json".toMediaTypeOrNull())


    /**
     * Возвращает ключ для файла.
     */
    private fun makeFileKey(file: FileAttachEntity) = file.uri.toString()


    data class FileEvent(
        val file: FileAttachEntity,
        val type: FileEventType
    )

    enum class FileEventType {
        DONE, REMOVE
    }

}