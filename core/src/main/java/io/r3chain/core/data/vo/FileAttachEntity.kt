package io.r3chain.core.data.vo

import android.net.Uri

data class FileAttachEntity(
    val uri: Uri,
    val isLoading: Boolean,
    val resource: ResourceEntity? = null,
    val error: String? = null
)
