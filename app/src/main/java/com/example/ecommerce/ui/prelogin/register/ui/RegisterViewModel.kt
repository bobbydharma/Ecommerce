package com.example.ecommerce.ui.prelogin.register.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.ui.prelogin.register.core.repository.RegisterRepository
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.asMutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
) : ViewModel() {

    private val _registerData =
        MutableStateFlow<Result<Unit>>(Result.Empty)
    val registerData = _registerData.asStateFlow()

    fun postRegister(registerRequest: UserRequest) {
        viewModelScope.launch {
            _registerData.asMutableStateFlow {
                repository.postRegister(registerRequest)
            }
        }
    }
}