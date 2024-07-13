package io.r3chain.core.data.repositories

import android.net.Uri
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.ResourceEntity
import kotlinx.coroutines.flow.SharedFlow

interface ResourcesGateway {

    /**
     * События отправляемых файлов.
     */
    val events: SharedFlow<FileEvent>

    /**
     * Запускает процесс загрузки файла.
     */
    suspend fun startUploadFile(file: FileAttachEntity)

    /**
     * Открепляет файл.
     */
    suspend fun removeFile(file: FileAttachEntity)

    /**
     * Получает данные файла по ключу.
     */
    fun getActualFileData(file: FileAttachEntity): FileAttachEntity?

    /**
     * Отправляет файл на сервер.
     */
    suspend fun sendFile(uri: Uri): Result<ResourceEntity>

    data class FileEvent(
        val file: FileAttachEntity,
        val type: FileEventType
    )

    enum class FileEventType {
        DONE, REMOVE
    }
}
