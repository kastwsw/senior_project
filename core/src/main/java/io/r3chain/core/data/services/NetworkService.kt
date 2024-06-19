package io.r3chain.core.data.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Отслеживает наличие доступа в Интернет.
 */
@Singleton
class NetworkService @Inject constructor(
    @ApplicationContext context: Context
) {

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

}