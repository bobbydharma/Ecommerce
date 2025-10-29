package com.example.ecommerce.ui.prelogin.login.core.di

import com.example.ecommerce.ui.prelogin.login.core.repository.LoginRepository
import com.example.ecommerce.ui.prelogin.login.core.repository.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository
}