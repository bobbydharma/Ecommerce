package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.core.model.rating.RatingRequest
import com.example.ecommerce.core.model.rating.RatingResponse
import com.example.ecommerce.ui.main.sendreview.SendReviewViewModel
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

@RunWith(JUnit4::class)
class SendReviewViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var repository: MainRepository
    private lateinit var sendReviewViewModel: SendReviewViewModel

    @Before
    fun setup() {
        repository = Mockito.mock()
        savedStateHandle = Mockito.mock()
        sendReviewViewModel = SendReviewViewModel(savedStateHandle, repository)
    }

    val ratingRequest = RatingRequest(
        invoiceId = "",
        rating = 0,
        review = ""
    )

    val ratingResponse = RatingResponse(
        code = 200,
        message = "Fulfillment rating and review success"
    )

    @Test
    fun `post rating send review viewmodel test success`() = runTest {
        whenever(repository.postRating(ratingRequest)).thenReturn(Result.Success(ratingResponse))
        sendReviewViewModel.postRating(ratingRequest)

        Assert.assertEquals(Result.Loading, sendReviewViewModel.ratingResponse.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(ratingResponse),
            sendReviewViewModel.ratingResponse.getOrAwaitValue()
        )
    }

    @Test
    fun `post rating send review viewmodel test error`() = runTest {
        val error = RuntimeException()
        whenever(repository.postRating(ratingRequest)).thenReturn(Result.Error(error))
        sendReviewViewModel.postRating(ratingRequest)

        Assert.assertEquals(Result.Loading, sendReviewViewModel.ratingResponse.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            sendReviewViewModel.ratingResponse.getOrAwaitValue()
        )
    }

}