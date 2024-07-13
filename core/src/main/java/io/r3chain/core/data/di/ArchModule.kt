package io.r3chain.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.r3chain.core.data.repositories.UserRepository
import io.r3chain.core.data.repositories.UserRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
interface ArchModule {

    @Binds
    fun auth(impl: UserRepositoryImpl): UserRepository
}