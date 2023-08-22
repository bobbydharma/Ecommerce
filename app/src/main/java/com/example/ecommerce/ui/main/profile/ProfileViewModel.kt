package com.example.ecommerce.ui.main.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.ProfileRequest
import com.example.ecommerce.model.ProfileResponse
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PreloginRepository
) : ViewModel() {

    var imageUri: Uri? = null

    private val _profileData = MutableLiveData<Result<ProfileResponse>>()
    val profileData: LiveData<Result<ProfileResponse>> = _profileData

    fun postProfile(profileRequest: ProfileRequest) {
        _profileData.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postProfile(profileRequest)
            _profileData.value = result
        }
    }

    fun uriToFile(uri: Uri, context: Context): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}