package com.example.ecommerce.ui.main.profile.core.di

import com.example.ecommerce.ui.main.profile.core.repository.ProfileRepository
import com.example.ecommerce.ui.main.profile.core.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryProfile(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}