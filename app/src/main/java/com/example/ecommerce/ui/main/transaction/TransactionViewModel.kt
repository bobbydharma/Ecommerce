package com.example.ecommerce.ui.main.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.model.transaction.TransactionResponse
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _transaction = MutableStateFlow<Result<TransactionResponse>>(Result.Loading)
    val transaction = _transaction

    suspend fun getTransaction() {
        viewModelScope.launch {
            repository.getTransaction()
                .catch {
                    _transaction.value = Result.Error(it)
                }.collect {
                    _transaction.value = Result.Success(it)
                }
        }
    }

}