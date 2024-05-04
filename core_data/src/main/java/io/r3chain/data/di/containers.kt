package io.r3chain.data.di

import io.r3chain.data.BuildConfig
import io.r3chain.data.api.infrastructure.ApiClient


// TODO: сделать нормальный DI на Hilt.
object DiContainer {

    /**
     * Rest API к серверу данных.
     */
    val apiClientGateway by lazy {
        ApiClient(baseUrl = BuildConfig.BASE_URL)
    }

}