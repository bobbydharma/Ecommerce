package com.example.ecommerce.network

import com.example.ecommerce.model.PrefHelper
import com.example.ecommerce.model.ProfileRequest
import com.example.ecommerce.model.ProfileResponse
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.model.UserResponse
import com.example.ecommerce.utils.Result
import java.lang.Exception
import javax.inject.Inject

class PreloginRepository @Inject constructor (
    private val APIService:APIService,
    private val sharedPreferencesManager: PrefHelper
) {
    companion object{
        const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    }

    suspend fun postRegister(registerRequest: UserRequest): com.example.ecommerce.utils.Result<UserResponse>{
        return try{
            val response = APIService.postRegister(API_KEY,registerRequest)
            if (response.isSuccessful){
                val userResponse = response.body()
                if (userResponse != null){
                    sharedPreferencesManager.token = userResponse.data.accessToken
                    sharedPreferencesManager.refreshToken = userResponse.data.refreshToken
                }
                com.example.ecommerce.utils.Result.Success(response.body()!!)
            }else{
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception){
            com.example.ecommerce.utils.Result.Error(e)
        }
    }

//    suspend fun postProfile(profileRequest: ProfileRequest): ProfileResponse{
//        val response = APIService.postProfile(API_KEY,profileRequest)
//        return ProfileResponse(response)
//    }

}