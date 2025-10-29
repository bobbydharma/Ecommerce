package com.example.ecommerce.ui.prelogin.login.core.repository

import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.repository.PreloginRepository.Companion.API_KEY
import com.example.ecommerce.ui.prelogin.login.core.mapper.toDomainModel
import com.example.ecommerce.ui.prelogin.login.core.model.LoginDomainModel
import com.example.ecommerce.utils.safeApiCall
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val sharedPref: PrefHelper
): LoginRepository {
    override suspend fun postLogin(loginRequest: UserRequest): LoginDomainModel {
        return safeApiCall {
            val response = apiService.postLogin(API_KEY, loginRequest)
            val domain = response.toDomainModel()

            response.data.let { data ->
                sharedPref.apply {
                    nama = data.userName
                    image_url = data.userImage
                    token = data.accessToken
                    refreshToken = data.refreshToken
                }
            }
            domain
        }
    }

}