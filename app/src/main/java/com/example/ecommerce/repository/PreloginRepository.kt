package com.example.ecommerce.repository

import com.example.ecommerce.core.model.user.LoginResponse
import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.core.model.user.ProfileResponse
import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.core.model.user.UserResponse
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.utils.ParsingError.getThrowable
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
                if (response.code() == 400) {
                    Result.Error(Exception("Email sudah digunakan"))
                } else {
                    Result.Error(Exception("Api key is not valid"))
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun postProfile(profileRequest: ProfileRequest): Result<ProfileResponse> {
        return try {
            val response = APIService.postProfile(
                profileRequest.userName,
                profileRequest.userImage
            )
            if (response.isSuccessful && response.body() != null) {
                val profileResponse = response.body()
                sharedPreferencesManager.nama = profileResponse?.data?.userName
                sharedPreferencesManager.image_url = profileResponse?.data?.userImage
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception(response.errorBody()?.getThrowable()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun postLogin(loginRequest: UserRequest): Result<LoginResponse> {
        return try {
            val response = APIService.postLogin(API_KEY, loginRequest)
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()
                sharedPreferencesManager.apply {
                    nama = loginResponse?.data?.userName
                    image_url = loginResponse?.data?.userImage
                    token = loginResponse?.data?.accessToken
                    refreshToken = loginResponse?.data?.refreshToken
                }
                Result.Success(response.body()!!)
            } else {
//                if (response.code() == 400) {
//                    Result.Error(Exception("Email or password is not valid"))
//                } else {
//                    Result.Error(Exception("Api key is not valid"))
//                }
                Result.Error(Exception(response.errorBody()?.getThrowable()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}