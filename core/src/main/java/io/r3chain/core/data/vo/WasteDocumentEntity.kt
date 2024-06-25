package io.r3chain.core.data.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WasteDocumentEntity(
    val id: Int = 0,
    val type: Int = 0
) : Parcelable