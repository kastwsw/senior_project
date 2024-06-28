package io.r3chain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.r3chain.feature.auth.AuthScreen
import io.r3chain.feature.auth.AuthScreenImpl
import io.r3chain.feature.inventory.InventoryScreen
import io.r3chain.feature.inventory.InventoryScreenImpl

@Module
@InstallIn(ViewModelComponent::class)
interface NavModule {

    @Binds
    fun auth(impl: AuthScreenImpl): AuthScreen

    @Binds
    fun inventory(impl: InventoryScreenImpl): InventoryScreen

}