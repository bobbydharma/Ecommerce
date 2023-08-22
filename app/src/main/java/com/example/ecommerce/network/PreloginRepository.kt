package com.example.ecommerce.network

import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.utils.Result
import java.lang.Exception
import javax.inject.Inject

class PreloginRepository @Inject constructor (private val APIService:APIService) {
    companion object{
        const val API_KEY = "6f8856ed-9189-488f-9011-0ff4b6c08edc"
    }

    suspend fun postRegister(registerRequest: UserRequest): com.example.ecommerce.utils.Result<UserRequest>{
        return try{
            val response = APIService.postRegister(API_KEY,registerRequest)
            if (response.isSuccessful){
                com.example.ecommerce.utils.Result.Success(response.body()!!)
            }else{
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception){
            com.example.ecommerce.utils.Result.Error(e)
        }
    }
}