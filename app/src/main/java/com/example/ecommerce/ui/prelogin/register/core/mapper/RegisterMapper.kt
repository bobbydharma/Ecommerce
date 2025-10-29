package com.example.ecommerce.ui.prelogin.register.core.mapper

import com.example.ecommerce.core.model.user.UserResponse
import com.example.ecommerce.ui.prelogin.register.core.model.RegisterDomainModel

fun UserResponse.toDomainModel(): RegisterDomainModel{
    return RegisterDomainModel(
        accessToken = data.accessToken ?: "",
        refreshToken = data.refreshToken ?: "",
        expiresAt = data.expiresAt ?: 0
    )
}