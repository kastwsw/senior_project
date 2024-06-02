package io.r3chain.data.vo

import io.r3chain.data.api.models.AuthDto
import io.r3chain.data.api.models.ResourceDto

data class UserVO (
    val id: Int = 0,
    val firstName: String = "",
    val email: String = "",
    val imageResourceID: Int = 0,
    val sendEmailNotifications: Boolean = false
) {

    fun createByApi(value: AuthDto) = copy(
        id = value.id,
        firstName = value.firstName,
        email = value.email,
        imageResourceID = value.imageResourceID,
        sendEmailNotifications = value.sendEmailNotifications
    )
}

data class ResourceVO (
    val id: Int = 0,
    val posterLink: String = "",
    val posterWidth: Int = 0,
    val posterHeight: Int = 0,
    val uiId: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {

    fun createByApi(value: ResourceDto) = copy(
        id = value.id,
        posterLink = value.posterFilePath,
        posterWidth = value.posterWidth,
        posterHeight = value.posterHeight,
        uiId = value.uiId ?: "",
        latitude = value.latitude,
        longitude = value.longitude
    )
}
