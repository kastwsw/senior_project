package io.r3chain.data.vo

data class WasteVO(
    val id: Int = 0,
    val picturesId: List<Int> = emptyList(),
    val location: Int = 0,
    val materialTypes: List<WasteType> = emptyList(),
    val date: String = "",
    val weight: String = "",
    val partner: String = "",
    val documents: List<WasteDocumentVO> = emptyList()
)