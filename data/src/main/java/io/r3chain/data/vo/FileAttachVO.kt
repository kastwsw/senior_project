package io.r3chain.data.vo

import android.net.Uri

data class FileAttachVO(
    val uri: Uri,
    val isLoading: Boolean,
    val resource: ResourceVO? = null,
    val error: String? = null
)
