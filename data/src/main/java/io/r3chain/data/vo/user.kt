package io.r3chain.data.vo

import io.r3chain.data.api.models.AuthDto

data class UserVO (
    val id: Int = 0,
    val firstName: String = "",
    val email: String = "",
    val sendEmailNotifications: Boolean = false
) {

    fun createByApi(value: AuthDto) = copy(
        id = value.id,
        firstName = value.firstName,
        email = value.email,
        sendEmailNotifications = value.sendEmailNotifications
    )
}