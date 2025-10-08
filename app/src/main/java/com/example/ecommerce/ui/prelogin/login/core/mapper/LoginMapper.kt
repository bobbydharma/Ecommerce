package com.example.ecommerce.ui.prelogin.login.core.mapper

import com.example.ecommerce.core.model.user.LoginResponse
import com.example.ecommerce.ui.prelogin.login.core.model.LoginDomainModel
import com.example.ecommerce.ui.prelogin.login.core.model.LoginUiModel

fun LoginResponse.toDomainModel(): LoginDomainModel {
    return LoginDomainModel(
        userName = data.userName ?: "",
        userImage = data.userImage ?: "",
        accessToken = data.accessToken ?: "",
        refreshToken = data.refreshToken ?: "",
        expiresAt = data.expiresAt ?: 0
    )
}

fun LoginDomainModel.toUiModel(): LoginUiModel =
    LoginUiModel(
        userName = userName
    )