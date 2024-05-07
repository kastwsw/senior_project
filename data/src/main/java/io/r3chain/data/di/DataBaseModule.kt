package io.r3chain.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.r3chain.data.db.CacheDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBaseModule {

    companion object {

        /**
         * База для кэша обнуляется при миграции.
         */
        @Singleton
        @Provides
        fun provideCacheDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context = context,
            klass = CacheDatabase::class.java,
            name = "cache_api"
        ).fallbackToDestructiveMigration().build()
    }
}