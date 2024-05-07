package io.r3chain.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.r3chain.data.BuildConfig
import io.r3chain.data.api.infrastructure.ApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    companion object {
        @Singleton
        @Provides
        fun provideApiClient(): ApiClient = ApiClient(baseUrl = BuildConfig.BASE_URL)

        // TODO: запровайдить БД
//        @Singleton
//        @Provides
//        fun provideRoom(@ApplicationContext context: Context): SomeBD = SomeBD(context = context)
    }
}