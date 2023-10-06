package com.example.ecommerce.repository

import com.example.ecommerce.core.model.checkout.FulfillmentRequest
import com.example.ecommerce.core.model.checkout.FulfillmentResponse
import com.example.ecommerce.core.model.products.ProductDetailResponse
import com.example.ecommerce.core.model.products.ReviewProduct
import com.example.ecommerce.core.model.products.SearchResponse
import com.example.ecommerce.core.model.rating.RatingRequest
import com.example.ecommerce.core.model.rating.RatingResponse
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val APIService: APIService,
) {
    suspend fun postSearch(search: String): Result<SearchResponse> {
        return try {
            val response = APIService.postSearch(search)
            if (response.isSuccessful && response.body() != null) {
                val searchProdact = response.body()
                Result.Success(searchProdact!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getProductDetail(id: String): Result<ProductDetailResponse> {
        return try {
            val response = APIService.getDetailProduct(id)
            if (response.isSuccessful && response.body() != null) {
                val detailProduct = response.body()
                Result.Success(detailProduct!!)
            } else {
                Result.Error(Exception("Gagal terhubung ke API"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

    suspend fun getReviewProduct(id: String): Result<ReviewProduct> {
        return try {
            val response = APIService.getReviewProduct(id)
            if (response.isSuccessful && response.body() != null) {
                val reviewProduct = response.body()
                Result.Success(reviewProduct!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

//    suspend fun postPayment() : Result<PaymentResponse>{
//        return try {
//            val response = APIService.postPayment()
//            if (response.isSuccessful && response.body() != null){
//                val paymentItem = response.body()
//                Result.Success(paymentItem!!)
//            }else{
//                Result.Error(Exception("API call failed"))
//            }
//        }catch (e: Exception){
//            Result.Error(e)
//        }
//    }

    suspend fun postFulfillment(fulfillmentRequest: FulfillmentRequest): Result<FulfillmentResponse> {
        return try {
            val response = APIService.postFulfillment(fulfillmentRequest)
            if (response.isSuccessful && response.body() != null) {
                val fulfillmentItem = response.body()
                Result.Success(fulfillmentItem!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun postRating(ratingRequest: RatingRequest): Result<RatingResponse> {
        return try {
            val response = APIService.postRating(ratingRequest)
            if (response.isSuccessful && response.body() != null) {
                val rating = response.body()
                Result.Success(rating!!)
            } else {
                Result.Error(Exception("API call failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getTransaction() = flow {
        emit(APIService.getTransaction())
    }.flowOn(Dispatchers.IO)
}