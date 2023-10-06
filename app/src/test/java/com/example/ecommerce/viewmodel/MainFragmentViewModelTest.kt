package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.NotificationRepository
import com.example.ecommerce.repository.WishlistRepository
import com.example.ecommerce.core.room.entity.CartEntity
import com.example.ecommerce.core.room.entity.NotificationEntity
import com.example.ecommerce.core.room.entity.WishlistEntity
import com.example.ecommerce.ui.main.MainViewModel
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
class MainFragmentViewModelTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var wishlistRepository: WishlistRepository
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var mainFragmenViewModel: MainViewModel

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

    private val notification = com.example.ecommerce.core.room.entity.NotificationEntity(
        idNotification = 1,
        title = "title",
        body = "body",
        image = "image",
        type = "Type",
        date = "date",
        time = "Time",
        isRead = false
    )

    val expected = flowOf(listOf(cartEntity))
    val isRead = false
    val expectedNotificationFlow = flowOf(listOf(notification))
    val expectedWishlistFlow = flowOf(listOf(wishlistEntity))

    @Before
    fun setup() {
        cartRepository = Mockito.mock()
        wishlistRepository = Mockito.mock()
        notificationRepository = Mockito.mock()

        whenever(cartRepository.getAll()).thenReturn(expected)
        whenever(wishlistRepository.getAll()).thenReturn(expectedWishlistFlow)
        whenever(notificationRepository.cekIsRead(isRead)).thenReturn(expectedNotificationFlow)

        mainFragmenViewModel =
            MainViewModel(cartRepository, wishlistRepository, notificationRepository)

    }

    @Test
    fun `cart item main fragment viewmodel test`() = runTest {
        val result = mainFragmenViewModel.cartItem
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `item wishlist main fragment viewmodel test`() = runTest {
        val result = mainFragmenViewModel.itemWishlist
        Assert.assertEquals(expectedWishlistFlow, result)
    }

    @Test
    fun `item notifcation main fragment viewmodel test`() = runTest {
        val result = mainFragmenViewModel.itemNotification
        Assert.assertEquals(expectedNotificationFlow, result)
    }

}