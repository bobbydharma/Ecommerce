package com.example.ecommerce.ui.main.store

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOG_TAG
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.di.AppModule
import com.example.ecommerce.model.products.Items
import com.example.ecommerce.model.products.ProductsRequest
import com.example.ecommerce.model.products.ProductsResponse
import com.example.ecommerce.model.products.SearchResponse
import com.example.ecommerce.network.APIService
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: MainRepository,
    private val apiService: APIService
): ViewModel(){

    private val _productsData = MutableLiveData<Result<ProductsResponse>>()
    val productsData: LiveData<Result<ProductsResponse>> = _productsData

    private val _searchData = MutableLiveData<Result<SearchResponse>>()
    val searchData : LiveData<Result<SearchResponse>> = _searchData

    private val _debouncedSearch = MutableSharedFlow<String>()
    val debouncedSearch: SharedFlow<String> = _debouncedSearch

    private val _prooductQuery = MutableStateFlow(ProductsRequest())
    val prooductQuery : StateFlow<ProductsRequest> = _prooductQuery

    var search: String? = null

    fun updateQuery(
        search: String?,
        brand: String?,
        sort:String?,
        lowest:Int?,
        highest:Int?
    ){
        _prooductQuery.update { it ->
            it.copy(
                search = search,
                brand = brand,
                sort = sort,
                lowest = lowest,
                highest = highest
            )
        }
    }

    val querySearchResults = _prooductQuery.flatMapLatest { query ->
        getPagerFlow(apiService, _prooductQuery.value)
    }.cachedIn(viewModelScope)

    fun getPagerFlow(apiService: APIService, productQuery: ProductsRequest): Flow<PagingData<Items>> {
        return Pager(
            PagingConfig(pageSize = 5, initialLoadSize = 5)
        ) {
            PagingSource(apiService, productQuery)
        }.flow
            .cachedIn(viewModelScope)
    }

    fun postSearch(search:String){
        _searchData.value = Result.Loading
        viewModelScope.launch {
            val result = repository.postSearch(search)
            _searchData.value = result
        }
    }

    init {
        getPagerFlow(apiService,prooductQuery.value)

        viewModelScope.launch {
            _debouncedSearch
                .debounce(1000) // Wait for 1 second
                .collect { searchTerm ->
                    postSearch(searchTerm)
                }
        }
    }

    fun setSearchTerm(searchTerm: String) {
        viewModelScope.launch {
            _debouncedSearch.emit(searchTerm)
        }
    }


}