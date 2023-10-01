package com.example.ecommerce.repository

import com.example.ecommerce.model.user.DataLoginResponse
import com.example.ecommerce.model.user.DataProfilResponse
import com.example.ecommerce.model.user.DataResponse
import com.example.ecommerce.model.user.LoginResponse
import com.example.ecommerce.model.user.ProfileRequest
import com.example.ecommerce.model.user.ProfileResponse
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.model.user.UserResponse
import com.example.ecommerce.network.APIService
import com.example.ecommerce.preference.PrefHelper
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

    private lateinit var dataSource: APIService
    private lateinit var preloginRepository: PreloginRepository
    private lateinit var prefHelper: PrefHelper

    @Before
    fun setup() {
        prefHelper = mock()
        dataSource = mock()
        preloginRepository = PreloginRepository(dataSource, prefHelper)
    }

    @Test
    fun postRegisterPreloginRepositoryTestSuccess() = runTest {
        val userRequest = UserRequest()

        val userResponse = UserResponse(
            code = 200,
            message = "OK",
            data = DataResponse(
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
        val userRequest = UserRequest()
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

        val profileResponse = ProfileResponse(
            code = 200,
            message = "OK",
            data = DataProfilResponse(
                userName = "Test",
                userImage = "userImage"
            )
        )

        val profileRequest = ProfileRequest(
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

        val profileRequest = ProfileRequest(
            userName = userNamePart,
            userImage = userImagePart
        )

        whenever(dataSource.postProfile(userNamePart, userImagePart)).thenThrow(error)
        val result = preloginRepository.postProfile(profileRequest)
        Assert.assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun postLoginPreloginRepositoryTestSuccess() = runTest {
        val userRequest = UserRequest()

        val loginResponse = LoginResponse(
            code = 200,
            message = "OK",
            data = DataLoginResponse(
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
        val userRequest = UserRequest()
        val error = RuntimeException()

        whenever(dataSource.postLogin(API_KEY, userRequest)).thenThrow(error)
        val result = preloginRepository.postLogin(userRequest)
        Assert.assertEquals(error, (result as Result.Error).exception)
    }
}