package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.CartRepository
import com.example.ecommerce.repository.PreloginRepository
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.cart.CartViewModel
import com.example.ecommerce.ui.prelogin.login.LoginViewModel
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: CartRepository
    private lateinit var cartViewModel: CartViewModel

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

    val expected = flowOf(listOf(cartEntity))

    @Before
    fun setup() {
        repository = Mockito.mock()

        whenever(repository.getAll()).thenReturn(expected)
        cartViewModel = CartViewModel(repository)
    }

    @Test
    fun `get all cart item cart viewmodel test`() = runTest {

        val result = cartViewModel.cartItem
        Assert.assertEquals(expected, result)

    }

    @Test
    fun `delete cart Cart ViewModel Test`() = runTest {
        whenever(repository.deleteCart(cartEntity)).thenReturn(Unit)
        val result = cartViewModel.deleteCart(cartEntity)

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `update quantity cart cart viewmodel test`() = runTest {
        val idProduct = ""
        val quantity = 1
        whenever(repository.updateQuantityCart(idProduct, quantity)).thenReturn(Unit)
        val result = cartViewModel.updateQuantityCart(idProduct, quantity)

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `update selected cart cart viewmodel test`() = runTest {
        val idProduct = ""
        val isSelected = true
        whenever(repository.updateSelectedCart(idProduct, isSelected)).thenReturn(Unit)
        val result = cartViewModel.updateSelectedCart(idProduct, isSelected)

        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `update all selected cart cart viewmodel test`() = runTest {
        val isSelected = true

        whenever(repository.updateAllSelectedCart(isSelected)).thenReturn(Unit)
        val result = cartViewModel.updateAllSelectedCart(isSelected)

        Assert.assertEquals(Unit, result)
    }
}