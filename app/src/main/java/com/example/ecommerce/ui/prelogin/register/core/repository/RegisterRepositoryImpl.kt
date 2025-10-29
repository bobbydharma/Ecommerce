package com.example.ecommerce.ui.prelogin.register.core.repository

import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.repository.PreloginRepository.Companion.API_KEY
import com.example.ecommerce.ui.prelogin.register.core.mapper.toDomainModel
import com.example.ecommerce.ui.prelogin.register.core.model.RegisterDomainModel
import com.example.ecommerce.utils.safeApiCall
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val sharedPref: PrefHelper
) : RegisterRepository {
    override suspend fun postRegister(registerRequest: UserRequest): RegisterDomainModel {
        return safeApiCall {
            val response = apiService.postRegister(API_KEY, registerRequest)
            val domain = response.toDomainModel()

            response.data.let { data ->
                sharedPref.apply {
                    token = data.accessToken
                    refreshToken = data.refreshToken
                }
            }
            domain
        }
    }

}