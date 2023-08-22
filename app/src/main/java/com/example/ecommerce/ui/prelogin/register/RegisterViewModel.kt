package com.example.ecommerce.ui.prelogin.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.model.UserResponse
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: PreloginRepository
) : ViewModel() {

    private val _registerData = MutableLiveData<Result<UserResponse>>()
    val registerData: LiveData<Result<UserResponse>> = _registerData

    fun postRegister(registerRequest: UserRequest) {
        _registerData.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postRegister(registerRequest)
            _registerData.value = result
        }
    }
}