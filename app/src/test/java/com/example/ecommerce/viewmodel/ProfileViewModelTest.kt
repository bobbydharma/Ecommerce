package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.model.user.DataProfilResponse
import com.example.ecommerce.model.user.ProfileRequest
import com.example.ecommerce.model.user.ProfileResponse
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.ui.main.profile.ProfileViewModel
import com.example.ecommerce.ui.prelogin.login.LoginViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: PreloginRepository
    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setup() {
        repository = Mockito.mock()
        profileViewModel = ProfileViewModel(repository)
    }

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

    @Test
    fun `post profile profile viewmodel test success`() = runTest {
        whenever(repository.postProfile(profileRequest)).thenReturn(Result.Success(profileResponse))
        profileViewModel.postProfile(profileRequest)

        Assert.assertEquals(Result.Loading, profileViewModel.profileData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(profileResponse),
            profileViewModel.profileData.getOrAwaitValue()
        )
    }

    @Test
    fun `post profile profile viewmodel test error`() = runTest {
        val error = RuntimeException()
        whenever(repository.postProfile(profileRequest)).thenReturn(Result.Error(error))
        profileViewModel.postProfile(profileRequest)

        Assert.assertEquals(Result.Loading, profileViewModel.profileData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            profileViewModel.profileData.getOrAwaitValue()
        )
    }

}