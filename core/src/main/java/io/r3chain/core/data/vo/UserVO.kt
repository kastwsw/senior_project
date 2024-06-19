package io.r3chain.core.data.vo

import io.r3chain.core.api.models.AuthDto

data class UserVO(
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
