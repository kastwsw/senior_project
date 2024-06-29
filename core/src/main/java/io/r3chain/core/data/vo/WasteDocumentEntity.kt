package io.r3chain.core.data.vo

data class WasteDocumentEntity(
    val type: WasteDocType = WasteDocType.SLIP,
    val id: Int = 0
)