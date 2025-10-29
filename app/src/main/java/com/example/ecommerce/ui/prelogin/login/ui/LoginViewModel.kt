package com.example.ecommerce.ui.prelogin.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.ui.prelogin.login.core.mapper.toUiModel
import com.example.ecommerce.ui.prelogin.login.core.model.LoginUiModel
import com.example.ecommerce.ui.prelogin.login.core.repository.LoginRepository
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.asMutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {
    private val _loginData =
        MutableStateFlow<Result<LoginUiModel>>(Result.Empty)
    val loginData = _loginData.asStateFlow()

    fun postLogin(loginRequest: UserRequest) {
        viewModelScope.launch {
            _loginData.asMutableStateFlow {
                repository.postLogin(loginRequest).toUiModel()
            }
        }
    }
}