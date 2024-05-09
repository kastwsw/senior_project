package io.r3chain.navigation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityService @Inject constructor(
    @ApplicationContext context: Context
) {

    private val _internetAvailableFlow = MutableStateFlow(false)
    val internetAvailableFlow = _internetAvailableFlow.asStateFlow()

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            // Сеть доступна, проверяем наличие Интернета
            checkInternetCapability(network)
        }

        override fun onLost(network: Network) {
            // Сеть потеряна
            _internetAvailableFlow.value = false
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            // Обновление состояния доступа к Интернету при изменении возможностей сети
            _internetAvailableFlow.value = networkCapabilities
                .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }

    init {
        // Register the callback to listen for network changes
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

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