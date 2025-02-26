package io.r3chain.core.data.vo

data class WasteDocEntity(
    val type: WasteDocType = WasteDocType.SLIP,
    val id: Int = 0,
    val files: List<FileAttachEntity> = emptyList(),
    val vehicleNumber: String = "",
    val files2: List<FileAttachEntity> = emptyList()
)