package com.example.ecommerce.ui.main.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.model.PrefHelper
import com.example.ecommerce.model.ProfileRequest
import com.example.ecommerce.model.ProfileResponse
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.model.UserResponse
import com.example.ecommerce.network.PreloginRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: PreloginRepository,
    private val sharedPreferencesManager: PrefHelper
    )  : ViewModel() {
    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _profileData = MutableLiveData<Result<ProfileResponse>>()
    val profileData: LiveData<Result<ProfileResponse>> = _profileData

    fun setImageUri(uri: Uri){
        _imageUri.value = uri
    }

    fun postProfile(profileRequest: ProfileRequest){
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