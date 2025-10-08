package com.example.ecommerce.ui.main.profile.core.repository

import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.ui.main.profile.core.model.ProfileDomainModel

interface ProfileRepository {
    suspend fun postProfile(profileRequest: ProfileRequest): ProfileDomainModel
}