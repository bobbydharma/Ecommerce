package com.example.ecommerce.auth

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CekAuthorization @Inject constructor() {
    private val _unAutorization = MutableLiveData<Boolean?>(false)
    val isDataLoaded: MutableLiveData<Boolean?>
        get() = _unAutorization

    fun setAutorization() {
        _unAutorization.postValue(true)
    }

    fun resetAuthorization(){
        _unAutorization.postValue(null)
    }
}