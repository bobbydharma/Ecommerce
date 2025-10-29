package com.example.ecommerce.ui.prelogin.login.core.repository

import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.ui.prelogin.login.core.model.LoginDomainModel

interface LoginRepository {
    suspend fun postLogin(loginRequest: UserRequest): LoginDomainModel
}