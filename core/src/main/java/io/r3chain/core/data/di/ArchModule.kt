package io.r3chain.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.r3chain.core.data.repositories.ResourcesGateway
import io.r3chain.core.data.repositories.ResourcesGatewayImpl
import io.r3chain.core.data.repositories.UserRepository
import io.r3chain.core.data.repositories.UserRepositoryImpl
import io.r3chain.core.data.repositories.WasteRepository
import io.r3chain.core.data.repositories.WasteRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ArchModule {

    @Binds
    fun bindResourcesGateway(impl: ResourcesGatewayImpl): ResourcesGateway

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindWasteRepository(impl: WasteRepositoryImpl): WasteRepository
}