package com.example.ecommerce.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.model.products.Items
import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.ProductsRequest
import com.example.ecommerce.model.products.ProductsResponse
import com.example.ecommerce.model.products.ReviewProduct
import com.example.ecommerce.model.products.SearchResponse
import com.example.ecommerce.network.APIService
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val APIService: APIService,
){
    suspend fun postSearch (search:String): Result<SearchResponse> {
        return try {
            val response = APIService.postSearch(search)
            if (response.isSuccessful && response.body() != null){
                val searchProdact = response.body()
                Result.Success(searchProdact!!)
            }else{
                Result.Error(Exception("API call failed"))
            }

        }catch (e:Exception){
            Result.Error(e)
        }
    }

    suspend fun getProductDetail (id : String): Result<ProductDetailResponse> {
        return try{
            val response = APIService.getDetailProduct(id)
            if (response.isSuccessful && response.body() != null){
                val detailProduct = response.body()
                Result.Success(detailProduct!!)
            }else{
                Result.Error(Exception("API call failed"))
            }
        }catch (e: Exception){
            Result.Error(e)
        }

    }

    suspend fun getReviewProduct (id : String): Result<ReviewProduct> {
        return try{
            val response = APIService.getReviewProduct(id)
            if (response.isSuccessful && response.body() != null){
                val reviewProduct = response.body()
                Result.Success(reviewProduct!!)
            }else{
                Result.Error(Exception("API call failed"))
            }
        }catch (e: Exception){
            Result.Error(e)
        }

    }

}