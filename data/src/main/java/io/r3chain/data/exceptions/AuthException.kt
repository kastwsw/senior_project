package io.r3chain.data.exceptions

import java.io.IOException

class AuthException(
    val type: Type,
    message: String?,
    val errors: List<String>?
) : IOException(message) {

    enum class Type {
        UNAUTHORIZED, VALIDATION_ERROR
    }
}