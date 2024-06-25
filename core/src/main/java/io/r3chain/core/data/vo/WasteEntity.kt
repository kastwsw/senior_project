package io.r3chain.core.data.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WasteEntity(
    val recordType: WasteRecordType = WasteRecordType.COLLECT,
    val id: Int = 0,
    val location: Int = 0,
    val geoLatLong: Pair<Double, Double>? = null,
    val materialTypes: List<WasteType> = emptyList(),
    val time: Long? = null,
    val grams: Long? = null,
    val partner: String = "",
    val venue: String = "",
    val recipient: String = "",
    val documents: List<WasteDocumentEntity> = emptyList(),
    val files: List<FileAttachEntity> = emptyList()
) : Parcelable