package io.r3chain.data.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.r3chain.data.BuildConfig
import io.r3chain.data.api.infrastructure.ApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    companion object {

        @Singleton
        @Provides
        fun provideApiClient() = ApiClient(baseUrl = BuildConfig.BASE_URL)
            .setLogger {
                Log.d("API Client", it)
            }
    }
}