package com.example.ecommerce.repository

import com.example.ecommerce.model.ProfileRequest
import com.example.ecommerce.model.ProfileResponse
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.model.UserResponse
import com.example.ecommerce.network.APIService
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import javax.inject.Inject

class PreloginRepository @Inject constructor(
    private val APIService: APIService,
    private val sharedPreferencesManager: PrefHelper
) {

    companion object {
        const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    }

    suspend fun postRegister(registerRequest: UserRequest): Result<UserResponse> {
        return try {
            val response = APIService.postRegister(API_KEY, registerRequest)
            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()
                sharedPreferencesManager.token = userResponse?.data?.accessToken
                sharedPreferencesManager.refreshToken = userResponse?.data?.refreshToken
                Result.Success(userResponse!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun postProfile(profileRequest: ProfileRequest): Result<ProfileResponse> {
        return try {
            val response = APIService.postProfile(
                sharedPreferencesManager.token!!,
                profileRequest.userName,
                profileRequest.userImage
            )
            if (response.isSuccessful && response.body() != null) {
                val profileResponse = response.body()
                sharedPreferencesManager.nama = profileResponse?.data?.userName
                sharedPreferencesManager.image_url = profileResponse?.data?.userImage
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}