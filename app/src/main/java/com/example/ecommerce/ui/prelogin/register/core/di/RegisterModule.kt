package com.example.ecommerce.ui.prelogin.register.core.di

import com.example.ecommerce.ui.prelogin.register.core.repository.RegisterRepository
import com.example.ecommerce.ui.prelogin.register.core.repository.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RegisterModule {

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(
        impl: RegisterRepositoryImpl
    ): RegisterRepository
}