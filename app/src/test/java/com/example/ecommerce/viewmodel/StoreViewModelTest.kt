package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.model.products.SearchResponse
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.store.StoreViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(JUnit4::class)
class StoreViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MainRepository
    private lateinit var apiService: com.example.ecommerce.core.network.APIService
    private lateinit var storeViewModel: StoreViewModel

    @Before
    fun setup() {
        repository = Mockito.mock()
        apiService = Mockito.mock()
    }

    @Test
    fun `post search store viewmodel test success`() = runTest {
        val search = "Lenovo"
        val searchResponse = com.example.ecommerce.core.model.products.SearchResponse(
            code = 200,
            message = "OK",
            data = listOf(
                "Lenovo Legion 3",
                "Lenovo Legion 5",
                "Lenovo Legion 7",
                "Lenovo Ideapad 3",
                "Lenovo Ideapad 5",
                "Lenovo Ideapad 7"
            )
        )
        storeViewModel = StoreViewModel(repository, apiService)

        whenever(repository.postSearch(search)).thenReturn(Result.Success(searchResponse))
        storeViewModel.postSearch(search)

        Assert.assertEquals(Result.Loading, storeViewModel.searchData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(searchResponse),
            storeViewModel.searchData.getOrAwaitValue()
        )
    }

    @Test
    fun `post search store viewmodel test error`() = runTest {
        val search = "Lenovo"
        val error = RuntimeException()
        storeViewModel = StoreViewModel(repository, apiService)

        whenever(repository.postSearch(search)).thenReturn(Result.Error(error))
        storeViewModel.postSearch(search)

        Assert.assertEquals(Result.Loading, storeViewModel.searchData.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            storeViewModel.searchData.getOrAwaitValue()
        )
    }
}