package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.transaction.Datum
import com.example.ecommerce.ui.main.transaction.Item
import com.example.ecommerce.ui.main.transaction.TransactionResponse
import com.example.ecommerce.ui.main.transaction.TransactionViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
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
class TransactionViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MainRepository
    private lateinit var transactionViewModel: TransactionViewModel

    @Before
    fun setup(){
        repository = Mockito.mock()
        transactionViewModel = TransactionViewModel(repository)
    }

    val transactionResponse = TransactionResponse(
        code = 200,
        message = "OK",
        data = listOf(
            Datum(
                invoiceId = "8cad85b1-a28f-42d8-9479-72ce4b7f3c7d",
                status = true,
                date = "09 Jun 2023",
                time = "09:05",
                payment = "Bank BCA",
                total = 48998000,
                items = listOf(
                    Item(
                        productId = "bee98108-660c-4ac0-97d3-63cdc1492f53",
                        variantName = "RAM 16GB",
                        quantity = 2
                    )
                ),
                rating = 4,
                review = "LGTM",
                image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/4/6/0a49c399-cf6b-47f5-91c9-8cbd0b86462d.jpg",
                name = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray"
            )
        )
    )

    @Test
    fun `get transaction transaction viewmodel test success`() = runTest {

        whenever(repository.getTransaction()).thenReturn(flowOf(transactionResponse))
        transactionViewModel.getTransaction()

        val value = mutableListOf<Result<TransactionResponse>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            transactionViewModel.transaction.toList(value)
        }
        advanceUntilIdle()

        Assert.assertEquals(listOf(Result.Loading, Result.Success(transactionResponse)), value)

    }

    @Test
    fun `get transaction transaction viewmodel test error`() = runTest {
        val error = RuntimeException()

        whenever(repository.getTransaction()).thenReturn(flow { throw error })
        transactionViewModel.getTransaction()

        val value = mutableListOf<Result<TransactionResponse>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            transactionViewModel.transaction.toList(value)
        }
        advanceUntilIdle()

        Assert.assertEquals(listOf(Result.Loading, Result.Error(error)), value)

    }

}