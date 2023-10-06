package com.example.ecommerce.repository

import com.example.ecommerce.core.model.checkout.Data
import com.example.ecommerce.core.model.checkout.FulfillmentRequest
import com.example.ecommerce.core.model.checkout.FulfillmentResponse
import com.example.ecommerce.core.model.checkout.ItemFullfillment
import com.example.ecommerce.core.model.rating.RatingRequest
import com.example.ecommerce.core.model.rating.RatingResponse
import com.example.ecommerce.core.model.transaction.Datum
import com.example.ecommerce.core.model.transaction.Item
import com.example.ecommerce.core.model.transaction.TransactionResponse
import com.example.ecommerce.utils.Result
import com.example.ecommerce.viewmodel.FlowDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(JUnit4::class)
class MainRepositoryTest {

    @get:Rule
    var rule = FlowDispatcher()

    private lateinit var dataSource: com.example.ecommerce.core.network.APIService
    private lateinit var mainRepository: MainRepository

    @Before
    fun setup() {
        dataSource = mock()
        mainRepository = MainRepository(dataSource)
    }

    @Test
    fun postSearchMainRepositoryTestSuccess() = runTest {
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
        whenever(dataSource.postSearch(search)).thenReturn(
            Response.success(searchResponse)
        )
        val result = mainRepository.postSearch(search)
        assertEquals(searchResponse, (result as Result.Success).data)
    }

    @Test
    fun postSearchMainRepositoryTestError() = runTest {
        val search = "Lenovo"
        val error = RuntimeException()

        whenever(dataSource.postSearch(search)).thenThrow(error)
        val result = mainRepository.postSearch(search)
        assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun getProductDetailMainRepositoryTestSuccess() = runTest {
        val productId = ""
        val detailResponse = com.example.ecommerce.core.model.products.ProductDetailResponse(
            code = 200,
            message = "OK",
            data = com.example.ecommerce.core.model.products.DataProductDetail(
                productId = "17b4714d-527a-4be2-84e2-e4c37c2b3292",
                productName = "ASUS ROG Strix G17 G713RM-R736H6G-O - Eclipse Gray",
                productPrice = 24499000,
                image = listOf("image_url"),
                brand = "Asus",
                description = "Description",
                store = "AsusStore",
                sale = 12,
                stock = 2,
                totalRating = 7,
                totalReview = 5,
                totalSatisfaction = 100,
                productRating = 5F,
                productVariant = listOf(
                    com.example.ecommerce.core.model.products.ProductVariant(
                        variantName = "RAM 16GB",
                        variantPrice = 0
                    ), com.example.ecommerce.core.model.products.ProductVariant(
                        variantName = "RAM 32GB",
                        variantPrice = 1000000
                    )
                )
            )
        )

        whenever(dataSource.getDetailProduct(productId)).thenReturn(
            Response.success(detailResponse)
        )

        val result = mainRepository.getProductDetail(productId)
        assertEquals(detailResponse, (result as Result.Success).data)

    }

    @Test
    fun getProductDetailMainRepositoryTestError() = runTest {
        val productId = ""
        val error = RuntimeException()

        whenever(dataSource.getDetailProduct(productId)).thenThrow(error)

        val result = mainRepository.getProductDetail(productId)
        assertEquals(error, (result as Result.Error).exception)

    }

    @Test
    fun getReviewProductMainRepositoryTestSuccess() = runTest {
        val productId = ""
        val reviewResponse = com.example.ecommerce.core.model.products.ReviewProduct(
            code = 200,
            message = "OK",
            data = listOf(
                com.example.ecommerce.core.model.products.DataReview(
                    userName = "John",
                    userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                    userRating = 4,
                    userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                ), com.example.ecommerce.core.model.products.DataReview(
                    userName = "Doe",
                    userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
                    userRating = 5,
                    userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
                )
            )
        )

        whenever(dataSource.getReviewProduct(productId)).thenReturn(
            Response.success(reviewResponse)
        )

        val result = mainRepository.getReviewProduct(productId)
        assertEquals(reviewResponse, (result as Result.Success).data)
    }

    @Test
    fun getReviewProductMainRepositoryTestError() = runTest {
        val productId = ""
        val error = RuntimeException()

        whenever(dataSource.getReviewProduct(productId)).thenThrow(error)

        val result = mainRepository.getReviewProduct(productId)
        assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun postFulfillmentMainRepositoryTestSuccess() = runTest {
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
                total = 48998000,
                rating = 5,
                review = ""
            )
        )

        whenever(dataSource.postFulfillment(fulfillmentRequest)).thenReturn(
            Response.success(fulfillmentResponse)
        )

        val result = mainRepository.postFulfillment(fulfillmentRequest)
        assertEquals(fulfillmentResponse, (result as Result.Success).data)
    }

    @Test
    fun postFulfillmentMainRepositoryTestError() = runTest {
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

        val error = RuntimeException()

        whenever(dataSource.postFulfillment(fulfillmentRequest)).thenThrow(error)

        val result = mainRepository.postFulfillment(fulfillmentRequest)
        assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun postRatingMainRepositoryTestSuccess() = runTest {
        val ratingRequest = RatingRequest(
            invoiceId = "",
            rating = 0,
            review = ""
        )

        val ratingResponse = RatingResponse(
            code = 200,
            message = "Fulfillment rating and review success"
        )

        whenever(dataSource.postRating(ratingRequest)).thenReturn(
            Response.success(ratingResponse)
        )

        val result = mainRepository.postRating(ratingRequest)
        assertEquals(ratingResponse, (result as Result.Success).data)
    }

    @Test
    fun postRatingMainRepositoryTestError() = runTest {
        val ratingRequest = RatingRequest(
            invoiceId = "",
            rating = 0,
            review = ""
        )
        val error = RuntimeException()

        whenever(dataSource.postRating(ratingRequest)).thenThrow(error)

        val result = mainRepository.postRating(ratingRequest)
        assertEquals(error, (result as Result.Error).exception)
    }

    @Test
    fun getTransactionMainRepositoryTestSuccess() = runTest(UnconfinedTestDispatcher()) {
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

        whenever(dataSource.getTransaction()).thenReturn(
            transactionResponse
        )

        val result = mainRepository.getTransaction()
        assertEquals(transactionResponse, result.first())
    }
}