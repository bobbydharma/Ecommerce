package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.ui.prelogin.register.ui.RegisterViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class RegisterViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PreloginRepository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        repository = Mockito.mock()
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `Post Register Register ViewModel Test Success`() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()

        val userResponse = com.example.ecommerce.core.model.user.UserResponse(
            code = 200,
            message = "OK",
            data = com.example.ecommerce.core.model.user.DataResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        whenever(repository.postRegister(userRequest)).thenReturn(Result.Success(userResponse))
        registerViewModel.postRegister(userRequest)

        Assert.assertEquals(Result.Loading, registerViewModel.registerData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(userResponse),
            registerViewModel.registerData.getOrAwaitValue()
        )
    }

    @Test
    fun `Post Register Register ViewModel Test Error`() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()

        val error = RuntimeException()
        whenever(repository.postRegister(userRequest)).thenReturn(Result.Error(error))
        registerViewModel.postRegister(userRequest)

        Assert.assertEquals(Result.Loading, registerViewModel.registerData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            registerViewModel.registerData.getOrAwaitValue()
        )
    }
}