package io.r3chain.data.exceptions

import java.io.IOException

/**
 * Ошибка, которой не должно быть в ожидаемой рабочей логике.
 */
class SurpriseException(message: String?) : IOException(message ?: "Server answer is unexpected.")