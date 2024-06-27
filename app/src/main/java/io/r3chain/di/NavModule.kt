package io.r3chain.di

import InventoryScreenImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.r3chain.feature.auth.AuthScreen
import io.r3chain.feature.auth.AuthScreenImpl
import io.r3chain.feature.inventory.InventoryScreen

@Module
@InstallIn(SingletonComponent::class)
interface NavModule {

    @Binds
    fun auth(impl: AuthScreenImpl): AuthScreen

    @Binds
    fun inventory(impl: InventoryScreenImpl): InventoryScreen

}