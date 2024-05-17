package io.r3chain.data.exceptions

import java.io.IOException

class NetworkIOException(
    message: String = "Network error occurred",
    cause: Throwable? = null
) : IOException(message, cause)
