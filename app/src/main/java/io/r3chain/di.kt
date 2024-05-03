package io.r3chain

import androidx.compose.runtime.compositionLocalOf
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.presentation.Presenter

val LocalPresenter = compositionLocalOf<Presenter> {
    error("No presenter in composition.")
}

//val LocalNavigation = compositionLocalOf<NavHostController> {
//    error("No navigation in composition.")
//}

// TODO: сделать нормальный DI на Hilt.
object DiContainer {

    /**
     * Rest API к серверу данных.
     */
    val apiClientGateway by lazy {
        ApiClient(baseUrl = BuildConfig.BASE_URL)
    }

}