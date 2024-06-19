package io.r3chain.core.data.vo

import io.r3chain.core.api.models.ResourceDto

data class ResourceVO(
    val id: Int = 0,
    val posterLink: String = "",
    val posterWidth: Int = 0,
    val posterHeight: Int = 0,
    val uiId: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val time: Long? = null
) {

    fun createByApi(value: ResourceDto) = copy(
        id = value.id,
        posterLink = value.posterFilePath,
        posterWidth = value.posterWidth,
        posterHeight = value.posterHeight,
        uiId = value.uiId ?: "",
        latitude = value.latitude,
        longitude = value.longitude,
        time = value.at?.toInstant()?.toEpochMilli()
    )
}
