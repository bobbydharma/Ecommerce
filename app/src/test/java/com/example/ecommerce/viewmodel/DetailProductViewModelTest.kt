package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.ProductVariant
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.repository.WishlistRepository
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity
import com.example.ecommerce.ui.main.detail.DetailProductViewModel
import com.example.ecommerce.ui.prelogin.login.LoginViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(JUnit4::class)
class DetailProductViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainRepository: MainRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var detailProductViewModel: DetailProductViewModel

    private val cartEntity = CartEntity(
        productId = "productId",
        productName = "productName",
        productPrice = 2000,
        image = "image",
        brand = "brand",
        description = "description",
        store = "store",
        sale = 10,
        stock = 10,
        totalRating = 5,
        totalReview = 5,
        totalSatisfaction = 10,
        productRating = 5F,
        variantName = "varian Name",
        variantPrice = 1000,
        quantity = 3,
        isSelected = false
    )

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
            productVariant = listOf(
                ProductVariant(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ), ProductVariant(
                    variantName = "RAM 32GB",
                    variantPrice = 1000000
                )
            )
        )
    )

    private val wishlistEntity = WishlistEntity(
        productId = "productId",
        productName = "productName",
        productPrice = 2000,
        image = "image",
        brand = "brand",
        description = "description",
        store = "store",
        sale = 10,
        stock = 10,
        totalRating = 5,
        totalReview = 5,
        totalSatisfaction = 10,
        productRating = 5F,
        varianName = "varian Name",
        varianPrice = 1000,
    )

    val dataProductDetail = DataProductDetail(
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

    val idProduct = ""
    val expectedWishlistResponse = MutableStateFlow(wishlistEntity)
    val expectedCartEntity = MutableStateFlow(cartEntity)
    @Before
    fun setup() = runTest{
        mainRepository = Mockito.mock()
        cartRepository = Mockito.mock()
        savedStateHandle = Mockito.mock()
        wishlistRepository = Mockito.mock()
        whenever(mainRepository.getProductDetail(idProduct)).thenReturn(Result.Success(detailResponse))
        whenever(wishlistRepository.cekItemWishlist(idProduct)).thenReturn(expectedWishlistResponse)
        whenever(cartRepository.getItem(idProduct)).thenReturn(expectedCartEntity)
        detailProductViewModel = DetailProductViewModel(
            mainRepository,
            cartRepository,
            savedStateHandle,
            wishlistRepository
        )
    }

    @Test
    fun `get detail product fetch data and get cart checkout viewmodel test`() = runTest {
        whenever(mainRepository.getProductDetail(idProduct)).thenReturn(Result.Success(detailResponse))
        whenever(wishlistRepository.cekItemWishlist(idProduct)).thenReturn(expectedWishlistResponse)
        whenever(cartRepository.getItem(idProduct)).thenReturn(expectedCartEntity)

        detailProductViewModel = DetailProductViewModel(
            mainRepository,
            cartRepository,
            savedStateHandle,
            wishlistRepository
        )

        Assert.assertEquals(Result.Loading, detailProductViewModel.detailProduct.getOrAwaitValue())
        advanceTimeBy(1)
        Assert.assertEquals(Result.Success(detailResponse), detailProductViewModel.detailProduct.getOrAwaitValue())
//        Assert.assertEquals(expectedWishlistResponse, detailProductViewModel.wishlistItem)
//        Assert.assertEquals(expectedCartEntity, detailProductViewModel.cartEntityFlow)
    }

    @Test
    fun `cek item detail product viewmodel test`() = runTest {
        val productId = ""
        whenever(cartRepository.cekItem(productId)).thenReturn(cartEntity)

        val result = detailProductViewModel.cekItem(productId)
        Assert.assertEquals(cartEntity, result)
    }

    @Test
    fun `insert or update item detail product viewmodel test`() = runTest {
        whenever(cartRepository.insertOrUpdateItem(cartEntity)).thenReturn(Unit)
        val result = detailProductViewModel.insertOrUpdateItem(dataProductDetail,0)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `insert to wishlidt detail product viewmodel test`() = runTest {
        whenever(wishlistRepository.insertToWishlist(wishlistEntity)).thenReturn(Unit)
        val result = detailProductViewModel.insertToWishlist(dataProductDetail)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `delete wishlist detail product viewmodel test`() = runTest {
        whenever(wishlistRepository.deleteWishlist(wishlistEntity)).thenReturn(Unit)
        val result = detailProductViewModel.deleteWishlist(dataProductDetail)
        Assert.assertEquals(Unit, result)
    }
}