package io.r3chain.core.data.vo

import android.os.Parcelable
import io.r3chain.core.api.models.ResourceDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResourceEntity(
    val id: Int = 0,
    val posterLink: String = "",
    val posterWidth: Int = 0,
    val posterHeight: Int = 0,
    val uiId: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val time: Long? = null
) : Parcelable {

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
