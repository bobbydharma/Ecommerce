package com.example.ecommerce.ui.main.profile.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.user.ProfileRequest
import com.example.ecommerce.core.model.user.ProfileResponse
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.ui.main.profile.core.mapper.toUiModel
import com.example.ecommerce.ui.main.profile.core.model.ProfileUiModel
import com.example.ecommerce.ui.main.profile.core.repository.ProfileRepository
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.asMutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    var imageUri: Uri? = null

    private val _profileData =
        MutableStateFlow<Result<ProfileUiModel>>(Result.Empty)
    val profileData = _profileData.asStateFlow()

    fun postProfile(profileRequest: ProfileRequest) {
        viewModelScope.launch {
            _profileData.asMutableStateFlow {
                repository.postProfile(profileRequest).toUiModel()
            }
        }
    }

}