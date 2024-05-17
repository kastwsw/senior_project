package io.r3chain.data.exceptions

import java.io.IOException

/**
 * Ошибка вызова метода API сервера.
 */
class ApiCallException(
    val code: Int,
    errorBody: String?
) : IOException("API call failed with status: $code and error: $errorBody")
