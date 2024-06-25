package io.r3chain.core.data.vo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileAttachEntity(
    val uri: Uri,
    val isLoading: Boolean,
    val resource: ResourceEntity? = null,
    val error: String? = null
) : Parcelable
