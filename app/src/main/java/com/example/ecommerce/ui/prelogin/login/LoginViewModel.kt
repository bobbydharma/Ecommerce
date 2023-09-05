package com.example.ecommerce.ui.prelogin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.user.LoginResponse
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: PreloginRepository
) : ViewModel(){
    private val _loginData = MutableLiveData<Result<LoginResponse>>()
    val loginData: LiveData<Result<LoginResponse>> = _loginData

    fun postLogin(loginRequest: UserRequest) {
        _loginData.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postLogin(loginRequest)
            _loginData.value = result
        }
    }
}