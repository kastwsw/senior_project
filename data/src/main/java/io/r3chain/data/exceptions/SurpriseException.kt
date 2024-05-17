package io.r3chain.data.exceptions

import java.io.IOException

class SurpriseException(message: String?) : IOException(message ?: "Server answer is unexpected.")