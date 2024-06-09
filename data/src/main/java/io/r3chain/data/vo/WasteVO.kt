package io.r3chain.data.vo

data class WasteVO(
    val id: Int = 0,
    val picturesId: List<Int> = emptyList(),
    val location: Int = 0,
    val geoLatLong: Pair<Double, Double>? = null,
    val materialTypes: List<WasteType> = emptyList(),
    val time: Long? = null,
    val grams: Long? = null,
    val partner: String = "",
    val documents: List<WasteDocumentVO> = emptyList()
)