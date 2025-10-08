package com.example.ecommerce.ui.main.profile.core.mapper

import com.example.ecommerce.core.model.user.ProfileResponse
import com.example.ecommerce.ui.main.profile.core.model.ProfileDomainModel
import com.example.ecommerce.ui.main.profile.core.model.ProfileUiModel

fun ProfileResponse.toDomainModel(): ProfileDomainModel{
    return ProfileDomainModel(
        userName = data.userName.orEmpty(),
        userImage = data.userImage.orEmpty()
    )
}

fun ProfileDomainModel.toUiModel(): ProfileUiModel{
    return ProfileUiModel(
        userName = userName,
        userImage = userImage
    )
}