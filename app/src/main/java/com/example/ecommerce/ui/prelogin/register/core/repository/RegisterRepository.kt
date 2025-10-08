package com.example.ecommerce.ui.prelogin.register.core.repository

import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.ui.prelogin.register.core.model.RegisterDomainModel

interface RegisterRepository {
    suspend fun postRegister(registerRequest: UserRequest): RegisterDomainModel
}