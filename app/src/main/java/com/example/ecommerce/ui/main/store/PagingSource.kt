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
                currentPage)
            if (response.isSuccessful && response.body() != null ){
                return LoadResult.Page(
                    data = response.body()!!.data.items,
                    prevKey = null,
                    nextKey = if(currentPage == response.body()!!.data.totalPages) null else currentPage +1
                )
            }else {
                if (response.code() == 404) {
                    return LoadResult.Error(Exception(response.code().toString()))
                } else {
                    return LoadResult.Error(Exception("Api key is not valid"))
                }
            }
        } catch (e: Exception) {
            return when(e){
                is HttpException -> {
                    LoadResult.Error(e)
                }

                is IOException -> {
                    LoadResult.Error(e)
                }

                else -> {

                    LoadResult.Error(e)
                }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Items>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}