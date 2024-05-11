package io.r3chain.data.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(
    @ApplicationContext context: Context
) {

    /*
        ДОСТУП В ИНТЕРНЕТ
     */

    private val _internetAvailableFlow = MutableStateFlow(false)

    /**
     * Есть или нет доступ в Интернет.
     */
    val internetAvailableFlow = _internetAvailableFlow.asStateFlow()

    /**
     * Системная служба контроля сетевых подключений.
     */
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Обработчик событий сетевых подключений.
     */
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Сеть доступна, проверяем наличие Интернета
            checkInternetCapability(network)
        }

        override fun onLost(network: Network) {
            // Сеть потеряна
            _internetAvailableFlow.value = false
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            // Обновление состояния доступа к Интернету при изменении возможностей сети
            _internetAvailableFlow.value = networkCapabilities
                .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }

    init {
        // Register the callback to listen for network changes
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    /**
     * Проверяет доступ в Интернет у определённого сетевого подключения.
     *
     * @param network Объект сетового подключения.
     */
    private fun checkInternetCapability(network: Network?) {
        // Получаем NetworkCapabilities для сети и проверяем наличие Интернета
        _internetAvailableFlow.value = connectivityManager
            .getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }


    /**
     * Форсированная проверка доступа в Интернет.
     */
    fun checkCurrentNetworkState() {
        checkInternetCapability(connectivityManager.activeNetwork)
    }

    /**
     * Clean-up method to be called when the service is no longer needed.
     */
    fun cleanup() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


    /*
        РАБОТА С API СЕРВЕРА
     */

    private val _exceptionsFlow = MutableSharedFlow<IOException>(
        // 0 - новые подписчики не получат предыдущие ошибки
        replay = 0,
        // хранить не более 10 ошибок
        extraBufferCapacity = 10,
        // удалять старые ошибоки в случае переполнения буфера
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Flow сетевых ошибок обращения к серверу.
     */
    val exceptionsFlow = _exceptionsFlow.asSharedFlow()

    suspend fun <T> safeApiCall(apiCall: suspend () -> T) = withContext(Dispatchers.IO) {
        // проверить доступ в интернет
        checkCurrentNetworkState()
        if (!internetAvailableFlow.value) {
            // добавить ошибку в flow ошибок
            val e = IOException("No internet connection.")
            _exceptionsFlow.emit(e)
            Result.failure(e)
        } else {
            // сделать запрос к серверу
            try {
                Result.success(apiCall())
            } catch (e: IOException) {
                // добавить ошибку в flow ошибок
                _exceptionsFlow.emit(e)
                // проверить доступ в интернет
                checkCurrentNetworkState()
                if (internetAvailableFlow.value) {
                    // TODO: логировать в Firebase
                    // серевер недоступен при активном доступе в Интернет
                    Log.d(this::class.simpleName, "Server did not answer.", e)
                }
                Result.failure(e)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
    }

}