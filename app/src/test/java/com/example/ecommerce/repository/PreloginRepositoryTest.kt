package com.example.ecommerce.repository

import com.example.ecommerce.core.model.user.DataLoginResponse
import com.example.ecommerce.core.model.user.DataProfilResponse
import com.example.ecommerce.core.model.user.DataResponse
import com.example.ecommerce.core.model.user.LoginResponse
import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.core.model.user.ProfileResponse
import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.core.model.user.UserResponse
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.repository.PreloginRepository.Companion.API_KEY
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(JUnit4::class)
class PreloginRepositoryTest {

    private lateinit var dataSource: com.example.ecommerce.core.network.APIService
    private lateinit var preloginRepository: PreloginRepository
    private lateinit var prefHelper: com.example.ecommerce.core.preference.PrefHelper

    @Before
    fun setup() {
        prefHelper = mock()
        dataSource = mock()
        preloginRepository = PreloginRepository(dataSource, prefHelper)
    }

    @Test
    fun postRegisterPreloginRepositoryTestSuccess() = runTest {
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

        whenever(dataSource.postRegister(API_KEY, userRequest)).thenReturn(
            Response.success(userResponse)
        )
        val result = preloginRepository.postRegister(userRequest)
        assertEquals(userResponse, (result as Result.Success).data)
    }

    @Test
    fun postRegisterPreloginRepositoryTestError() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()
        val error = RuntimeException()

        whenever(dataSource.postRegister(API_KEY, userRequest)).thenThrow(error)
        val result = preloginRepository.postRegister(userRequest)
        assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun postProfilePreloginRepositoryTestSuccess() = runTest {
        val userImageRequestBody = "your_image_data".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val userImagePart =
            MultipartBody.Part.createFormData("userImage", "image.jpg", userImageRequestBody)

        val userNameRequestBody = "John Doe".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart =
            MultipartBody.Part.createFormData("userName", "John Doe", userNameRequestBody)

        val profileResponse = com.example.ecommerce.core.model.user.ProfileResponse(
            code = 200,
            message = "OK",
            data = com.example.ecommerce.core.model.user.DataProfilResponse(
                userName = "Test",
                userImage = "userImage"
            )
        )

        val profileRequest = com.example.ecommerce.core.model.user.ProfileRequest(
            userName = userNamePart,
            userImage = userImagePart
        )

        whenever(dataSource.postProfile(userNamePart, userImagePart)).thenReturn(
            Response.success(profileResponse)
        )
        val result = preloginRepository.postProfile(profileRequest)
        Assert.assertEquals(profileResponse, (result as Result.Success).data)
    }

    @Test
    fun postProfilePreloginRepositoryTestError() = runTest {
        val userImageRequestBody = "your_image_data".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val userImagePart =
            MultipartBody.Part.createFormData("userImage", "image.jpg", userImageRequestBody)

        val userNameRequestBody = "John Doe".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart =
            MultipartBody.Part.createFormData("userName", "John Doe", userNameRequestBody)

        val error = RuntimeException()

        val profileRequest = com.example.ecommerce.core.model.user.ProfileRequest(
            userName = userNamePart,
            userImage = userImagePart
        )

        whenever(dataSource.postProfile(userNamePart, userImagePart)).thenThrow(error)
        val result = preloginRepository.postProfile(profileRequest)
        Assert.assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun postLoginPreloginRepositoryTestSuccess() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()

        val loginResponse = com.example.ecommerce.core.model.user.LoginResponse(
            code = 200,
            message = "OK",
            data = com.example.ecommerce.core.model.user.DataLoginResponse(
                userName = "userName",
                userImage = "userImage",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        whenever(dataSource.postLogin(API_KEY, userRequest)).thenReturn(
            Response.success(loginResponse)
        )
        val result = preloginRepository.postLogin(userRequest)
        Assert.assertEquals(loginResponse, (result as Result.Success).data)
    }

    @Test
    fun postLoginPreloginRepositoryTestError() = runTest {
        val userRequest = com.example.ecommerce.core.model.user.UserRequest()
        val error = RuntimeException()

        whenever(dataSource.postLogin(API_KEY, userRequest)).thenThrow(error)
        val result = preloginRepository.postLogin(userRequest)
        Assert.assertEquals(error, (result as Result.Error).exception)
    }
}