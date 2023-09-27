package com.example.ecommerce.network

import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.model.products.DataReview
import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.ProductVariant
import com.example.ecommerce.model.products.ReviewProduct
import com.example.ecommerce.model.products.SearchResponse
import com.example.ecommerce.model.user.DataLoginResponse
import com.example.ecommerce.model.user.DataProfilResponse
import com.example.ecommerce.model.user.DataResponse
import com.example.ecommerce.model.user.LoginResponse
import com.example.ecommerce.model.user.ProfileResponse
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.model.user.UserResponse
import com.example.ecommerce.network.NetworkUtilsTest.createMockResponse
import com.example.ecommerce.ui.main.checkout.Data
import com.example.ecommerce.ui.main.checkout.FulfillmentRequest
import com.example.ecommerce.ui.main.checkout.FulfillmentResponse
import com.example.ecommerce.ui.main.checkout.ItemFullfillment
import com.example.ecommerce.ui.main.sendreview.RatingRequest
import com.example.ecommerce.ui.main.sendreview.RatingResponse
import com.example.ecommerce.ui.main.transaction.Datum
import com.example.ecommerce.ui.main.transaction.Item
import com.example.ecommerce.ui.main.transaction.TransactionResponse
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import org.junit.Assert.assertEquals

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: APIService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create()
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun postRegisterTestApiService() = runTest {
        val response = createMockResponse("User_Response.json")
        mockWebServer.enqueue(response)

        val apiKey = "api_key"
        val userRequest = UserRequest()

        val userResponse = UserResponse(
            code = 200,
            message = "OK",
            data = DataResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        val apiResponse = apiService.postRegister(apiKey, userRequest)
        assertEquals(apiResponse.body(), userResponse)
    }

    @Test
    fun postProfileTestApiService() = runTest {
        val response = createMockResponse("Profile_Response.json")
        mockWebServer.enqueue(response)

        val authorization = "api_key"
        val userImageRequestBody = "your_image_data".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val userImagePart = MultipartBody.Part.createFormData("userImage", "image.jpg", userImageRequestBody)

        val userNameRequestBody = "John Doe".toRequestBody("text/plain".toMediaTypeOrNull())
        val userNamePart = MultipartBody.Part.createFormData("userName", "John Doe", userNameRequestBody)


        val profileResponse = ProfileResponse(
            code = 200,
            message = "OK",
            data = DataProfilResponse(
                userName = "Test",
                userImage = "userImage"
            )
        )

        val apiResponse = apiService.postProfile( userImagePart, userNamePart)
        assertEquals(apiResponse.body(), profileResponse)
    }

    @Test
    fun postLoginTestApiService() = runTest {
        val response = createMockResponse("Login_Response.json")
        mockWebServer.enqueue(response)

        val apiKey = "api_key"
        val userRequest = UserRequest()

        val loginResponse = LoginResponse(
            code = 200,
            message = "OK",
            data = DataLoginResponse(
                userName = "userName",
                userImage = "userImage",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        val apiResponse = apiService.postLogin(apiKey, userRequest)
        assertEquals(apiResponse.body(), loginResponse)
    }

    @Test
    fun postSearchTestApiService() = runTest {
        val response = createMockResponse("Search_Response.json")
        mockWebServer.enqueue(response)

        val query = "query"

        val searchResponse =SearchResponse(
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

        val apiResponse = apiService.postSearch(query)
        assertEquals(apiResponse.body(), searchResponse)
    }

    @Test
    fun postRefreshTokenTestApiService() = runTest {
        val response = createMockResponse("User_Response.json")
        mockWebServer.enqueue(response)

        val apiKey = "api_key"
        val query = "query_search"

        val userResponse = UserResponse(
            code = 200,
            message = "OK",
            data = DataResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )

        val apiResponse = apiService.postRefresh(apiKey, hashMapOf("token" to query))
        assertEquals(apiResponse.body(), userResponse)
    }

    @Test
    fun getDetailProductTestApiService() = runTest {
        val response = createMockResponse("Product_Detail_Response.json")
        mockWebServer.enqueue(response)

        val productId = ""
        val detailResponse = ProductDetailResponse(
            code = 200,
        message = "OK",
        data = DataProductDetail(
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
            productVariant = listOf(ProductVariant(
                variantName = "RAM 16GB",
                variantPrice = 0
            ), ProductVariant(
                variantName = "RAM 32GB",
                variantPrice = 1000000
                ))
        )
        )

        val apiResponse = apiService.getDetailProduct(productId)
        assertEquals(apiResponse.body(), detailResponse)
    }

    @Test
    fun getReviewProductTestApiService() = runTest {
        val response = createMockResponse("Review_Response.json")
        mockWebServer.enqueue(response)

        val productId = ""

        val reviewResponse = ReviewProduct(
            code= 200,
            message = "OK",
            data = listOf(DataReview(
                userName = "John",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQM4VpzpVw8mR2j9_gDajEthwY3KCOWJ1tOhcv47-H9o1a-s9GRPxdb_6G9YZdGfv0HIg&usqp=CAU",
                userRating = 4,
                userReview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
            ), DataReview(
                userName = "Doe",
                userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3Z6PN8QNVhH0e7rEINu_XJS0qHIFpDT3nwF5WSkcYmr3znhY7LOTkc8puJ68Bts-TMc&usqp=CAU",
                userRating = 5,
                userReview = "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
        ))
        )

        val apiResponse = apiService.getReviewProduct(productId)
        assertEquals(apiResponse.body(), reviewResponse)
    }

    @Test
    fun postFulfillmentTestApiService() =  runTest {
        val response = createMockResponse("Fulfillment_Response.json")
        mockWebServer.enqueue(response)

        val fulfillmentRequest = FulfillmentRequest(
            payment = "",
            items = listOf(ItemFullfillment(
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

        val apiResponse = apiService.postFulfillment(fulfillmentRequest)
        assertEquals(apiResponse.body(), fulfillmentResponse)
    }

    @Test
    fun postRatingTestApiService() = runTest {
        val response = createMockResponse("Rating_Response.json")
        mockWebServer.enqueue(response)

        val ratingRequest = RatingRequest(
            invoiceId = "",
            rating = 0,
            review = ""
        )

        val ratingResponse = RatingResponse(
            code = 200,
            message = "Fulfillment rating and review success"
        )

        val apiResponse = apiService.postRating(ratingRequest)
        assertEquals(apiResponse.body(), ratingResponse)
    }

    @Test
    fun getTransactionTestApiService() = runTest {
        val response = createMockResponse("Transaction_Response.json")
        mockWebServer.enqueue(response)

        val transactionResponse = TransactionResponse(
            code = 200,
            message = "OK",
            data = listOf(Datum(
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

        val apiResponse = apiService.getTransaction()
        assertEquals(apiResponse, transactionResponse)
    }

}