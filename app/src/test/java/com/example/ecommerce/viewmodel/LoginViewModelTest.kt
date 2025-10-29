package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.ui.prelogin.login.ui.LoginViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class LoginViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PreloginRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        repository = mock()
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun postLoginLoginViewModelTestSuccess() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()

        val userResponse = com.example.ecommerce.core.model.user.LoginResponse(
            code = 200,
            message = "OK",
            data = com.example.ecommerce.core.model.user.DataLoginResponse(
                userName = "",
                userImage = "",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        whenever(repository.postLogin(userRequest)).thenReturn(Result.Success(userResponse))
        loginViewModel.postLogin(userRequest)

        assertEquals(Result.Loading, loginViewModel.loginData.getOrAwaitValue())
        advanceTimeBy(1)
        assertEquals(Result.Success(userResponse), loginViewModel.loginData.getOrAwaitValue())

    }

    @Test
    fun postLoginLoginViewModelTestError() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()

        val error = RuntimeException()
        whenever(repository.postLogin(userRequest)).thenReturn(Result.Error(error))
        loginViewModel.postLogin(userRequest)

        assertEquals(Result.Loading, loginViewModel.loginData.getOrAwaitValue())
        advanceTimeBy(1)
        assertEquals(Result.Error(error), loginViewModel.loginData.getOrAwaitValue())
    }
}