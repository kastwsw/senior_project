package io.r3chain.core.data.exceptions

import java.io.IOException

/**
 * Нет выхода в Интернет.
 */
class NoInternetException(message: String? = null) : IOException(message ?: "No internet connection.")