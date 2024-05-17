package io.r3chain.data

import android.util.Log
import io.r3chain.data.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Логирует события в аппе.
 */
@Singleton
class LoggerService @Inject constructor(
    private val apiService: ApiService
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        // собирать ошибки обращений к API
        scope.launch {
            apiService.exceptionsFlow.collect { exception ->
                // TODO: лог в Firebase
//                if (exception !is NoInternetException) {
//                    Log...
//                }
                Log.d(exception::class.simpleName, exception.message, exception.cause)
            }
        }
    }

    fun cancel() {
        scope.cancel()
    }

}