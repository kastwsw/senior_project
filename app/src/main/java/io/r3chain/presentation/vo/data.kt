package io.r3chain.presentation.vo

import io.r3chain.data.api.models.AuthDto

data class UserVO (
    val id: Int = 0,
    val firstName: String = ""
) {
    fun createByApi(value: AuthDto) = copy(
        id = value.id,
        firstName = value.firstName
    )

}