package com.example.ecommerce.ui.main.store

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.model.products.Items
import com.example.ecommerce.model.products.ProductsRequest
import com.example.ecommerce.network.APIService
import com.example.ecommerce.utils.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PagingSource(
    private val apiService: APIService,
    private val query: ProductsRequest
) : PagingSource<Int, Items>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Items> {
        try {
            val currentPage = params.key ?: 1
            val response = apiService.postProducts(
                query.search,
                query.brand,
                query.lowest,
                query.highest,
                query.sort,
                query.limit,
                currentPage
            )
            if (response.isSuccessful && response.body() != null) {
                return LoadResult.Page(
                    data = response.body()!!.data.items,
                    prevKey = null,
                    nextKey = if (currentPage == response.body()!!.data.totalPages) null else currentPage + 1
                )
            } else {
                if (response.code() == 404) {
                    Log.d("404", "${response.code()}")
                    return LoadResult.Error(MyCustomError(response.code().toString()))
                } else {
                    Log.d("Else 404", "error")
                    return LoadResult.Error(HttpException(response))
                }
            }
        } catch (e: Exception) {
            return when (e) {
                is HttpException -> {
                    Log.d("HttpException", "${e.code()}")
                    LoadResult.Error(e)
                }

                is IOException -> {
                    Log.d("IOException", "${e.message}")
                    LoadResult.Error(e)
                }

                else -> {
                    Log.d("Else", "${e.message}")
                    LoadResult.Error(e)
                }
            }
        }
    }

    class MyCustomError(message: String) : Throwable(message)

    override fun getRefreshKey(state: PagingState<Int, Items>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}