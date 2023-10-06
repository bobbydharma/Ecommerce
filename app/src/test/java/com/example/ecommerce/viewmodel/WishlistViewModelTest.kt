package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.model.products.DataProductDetail
import com.example.ecommerce.core.model.products.ProductVariant
import com.example.ecommerce.core.network.APIService
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.repository.WishlistRepository
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.WishlistEntity
import com.example.ecommerce.ui.main.store.StoreViewModel
import com.example.ecommerce.ui.main.wishlist.WishlistViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class WishlistViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

//    @get:Rule
//    var flowDispatcher = FlowDispatcher()

    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var wishlistViewModel: WishlistViewModel

    @Before
    fun setup() {
        wishlistRepository = Mockito.mock()
        cartRepository = Mockito.mock()
        whenever(wishlistRepository.getAll()).thenReturn(flowOf(listOf(wishlistEntity)))
        wishlistViewModel = WishlistViewModel(wishlistRepository, cartRepository)
    }

    private val wishlistEntity = com.example.ecommerce.core.room.entity.WishlistEntity(
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

    private val cartEntity = com.example.ecommerce.core.room.entity.CartEntity(
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

    private val dataProductDetail = com.example.ecommerce.core.model.products.DataProductDetail(
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

    @Test
    fun `get all wishlist viewmodel test`() = runTest {
        whenever(wishlistRepository.getAll()).thenReturn(flowOf(listOf(wishlistEntity)))
        val result = wishlistViewModel.wishlistItem
        Assert.assertEquals(listOf(wishlistEntity), result.first())
    }

    @Test
    fun `delete wishlist wishlist viewmodel test`() = runTest {
        whenever(wishlistRepository.deleteWishlist(wishlistEntity)).thenReturn(Unit)
        val result = wishlistViewModel.deleteWishlist(wishlistEntity)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `insert or update cart wishlist viewmodel test`() = runTest {
        whenever(cartRepository.insertOrUpdateItem(cartEntity)).thenReturn(Unit)
        val result = wishlistViewModel.insertOrUpdateItem(dataProductDetail, 0)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `cek item wishlist viewmodel test`() = runTest {
        val productId = ""
        whenever(cartRepository.cekItem(productId)).thenReturn(cartEntity)

        val result = wishlistViewModel.cekItem(productId)
        Assert.assertEquals(cartEntity, result)
    }

}