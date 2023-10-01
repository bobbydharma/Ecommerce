package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.checkout.CheckoutViewModel
import com.example.ecommerce.ui.main.checkout.Data
import com.example.ecommerce.ui.main.checkout.FulfillmentRequest
import com.example.ecommerce.ui.main.checkout.FulfillmentResponse
import com.example.ecommerce.ui.main.checkout.ItemFullfillment
import com.example.ecommerce.ui.prelogin.login.LoginViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class CheckoutViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MainRepository
    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        savedStateHandle = Mockito.mock()
        repository = Mockito.mock()
        checkoutViewModel = CheckoutViewModel(savedStateHandle, repository)
    }

    val fulfillmentRequest = FulfillmentRequest(
        payment = "",
        items = listOf(
            ItemFullfillment(
                productId = "",
                variantName = "",
                quantity = 1
            )
        )
    )

    val fulfillmentResponse = FulfillmentResponse(
        code = 200,
        message = "OK",
        data = Data(
            invoiceId = "ba47402c-d263-49d3-a1f8-759ae59fa4a1",
            status = true,
            date = "09 Jun 2023",
            time = "08:53",
            payment = "Bank BCA",
            total = 48998000
        )
    )

    @Test
    fun `post fulfillment checkout viewmodel success`() = runTest {
        whenever(repository.postFulfillment(fulfillmentRequest)).thenReturn(
            Result.Success(
                fulfillmentResponse
            )
        )
        checkoutViewModel.postFulfillment(fulfillmentRequest)

        Assert.assertEquals(Result.Loading, checkoutViewModel.fulfillment.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Success(fulfillmentResponse),
            checkoutViewModel.fulfillment.getOrAwaitValue()
        )
    }

    @Test
    fun `post fulfillment checkout viewmodel error`() = runTest {
        val error = RuntimeException()
        whenever(repository.postFulfillment(fulfillmentRequest)).thenReturn(Result.Error(error))
        checkoutViewModel.postFulfillment(fulfillmentRequest)

        Assert.assertEquals(Result.Loading, checkoutViewModel.fulfillment.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(
            Result.Error(error),
            checkoutViewModel.fulfillment.getOrAwaitValue()
        )
    }
}