package io.r3chain.data.exceptions

import java.io.IOException

/**
 * IO ошибка обращения к серверу.
 */
class NetworkIOException(
    message: String = "Network error occurred",
    cause: Throwable? = null
) : IOException(message, cause)
