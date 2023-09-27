package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.ecommerce.model.products.DataReview
import com.example.ecommerce.model.products.ReviewProduct
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.detail.ReviewViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class ReviewViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainRepository: MainRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var reviewViewModel: ReviewViewModel
    val idProduct = ""
    val reviewResponse = ReviewProduct(
        code= 200,
        message = "OK",
        data = listOf(
            DataReview(
            userName = "John",
            userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
            userRating = 4,
            userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        ), DataReview(
            userName = "Doe",
            userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
            userRating = 5,
            userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
        )
        )
    )

    @Before
    fun setup() = runTest{
        mainRepository = Mockito.mock()
        savedStateHandle = Mockito.mock()
        whenever(mainRepository.getReviewProduct(idProduct)).thenReturn(Result.Success(reviewResponse))
        reviewViewModel = ReviewViewModel(mainRepository,savedStateHandle)
    }

    @Test
    fun `get review product review viewmodel test success`() = runTest {
        reviewViewModel.getReviewProduct(idProduct)

        Assert.assertEquals(Result.Loading, reviewViewModel.reviewProduct.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(reviewResponse),
            reviewViewModel.reviewProduct.getOrAwaitValue()
        )
    }

    @Test
    fun `get review product review viewmodel test error`() = runTest {
        reviewViewModel.getReviewProduct(idProduct)
        val error = RuntimeException()
        whenever(mainRepository.getReviewProduct(idProduct)).thenReturn(Result.Error(error))

        Assert.assertEquals(Result.Loading, reviewViewModel.reviewProduct.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            reviewViewModel.reviewProduct.getOrAwaitValue()
        )
    }
}