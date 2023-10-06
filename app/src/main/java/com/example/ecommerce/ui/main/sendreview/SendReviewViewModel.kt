package com.example.ecommerce.ui.main.sendreview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.rating.RatingRequest
import com.example.ecommerce.core.model.rating.RatingResponse
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.core.model.checkout.Data
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MainRepository
) : ViewModel() {
    val invoice = savedStateHandle.get<Data>("FulfillmentResponse")
    val sourceFragment = savedStateHandle.get<String>("SourceFragment")

    private val _ratingResponse = MutableLiveData<Result<RatingResponse>>()
    val ratingResponse: LiveData<Result<RatingResponse>> = _ratingResponse
    suspend fun postRating(ratingRequest: RatingRequest) {
        _ratingResponse.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postRating(ratingRequest)
            _ratingResponse.value = result
        }
    }
}