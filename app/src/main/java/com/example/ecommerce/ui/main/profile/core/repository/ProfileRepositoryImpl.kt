package com.example.ecommerce.ui.main.profile.core.repository

import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.repository.PreloginRepository.Companion.API_KEY
import com.example.ecommerce.ui.main.profile.core.mapper.toDomainModel
import com.example.ecommerce.ui.main.profile.core.model.ProfileDomainModel
import com.example.ecommerce.ui.prelogin.register.core.mapper.toDomainModel
import com.example.ecommerce.utils.safeApiCall
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val sharedPref: PrefHelper
) : ProfileRepository {
    override suspend fun postProfile(profileRequest: ProfileRequest): ProfileDomainModel {
        return safeApiCall {
            val response = apiService.postProfile(
                profileRequest.userName,
                profileRequest.userImage
            )
            val domain = response.toDomainModel()

            response.data.let { data ->
                sharedPref.apply {
                    nama = data.userName
                    image_url = data.userImage
                }
            }
            domain
        }
    }
}