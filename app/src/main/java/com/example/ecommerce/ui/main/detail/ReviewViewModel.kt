package com.example.ecommerce.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.products.ReviewProduct
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: MainRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _reviewProduct =
        MutableLiveData<Result<com.example.ecommerce.core.model.products.ReviewProduct>>()
    val reviewProduct: LiveData<Result<com.example.ecommerce.core.model.products.ReviewProduct>> =
        _reviewProduct

    val id = savedStateHandle.get<String>("id_product_review") ?: ""

    init {
        getReviewProduct(id)
    }

    fun getReviewProduct(id: String) {
        _reviewProduct.value = Result.Loading
        viewModelScope.launch {
            val result = repository.getReviewProduct(id)
            _reviewProduct.value = result
        }
    }

}