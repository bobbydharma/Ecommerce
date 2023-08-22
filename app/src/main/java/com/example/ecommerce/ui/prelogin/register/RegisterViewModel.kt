package com.example.ecommerce.ui.prelogin.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.network.PreloginRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (private val repository: PreloginRepository) :ViewModel() {

    private val _registerData = MutableLiveData<Result<UserRequest>>()
    val registerData: LiveData<Result<UserRequest>> = _registerData

    fun postRegister(registerRequest: UserRequest){
        viewModelScope.launch {
            _registerData.value = Result.Loading
            val result = repository.postRegister(registerRequest)
            _registerData.value = result
        }
    }
}