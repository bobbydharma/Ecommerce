package com.example.ecommerce.repository

import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class CartRepositoryTest {
    private lateinit var cartDAO: CartDAO
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartDAO = mock()
        cartRepository = CartRepository(cartDAO)
    }

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

    @Test
    fun getAllAndInsertCartRepositoryTest() = runTest {
        whenever(cartDAO.getAll()).thenReturn(flowOf(listOf(cartEntity)))
        val result = cartRepository.getAll()
        Assert.assertEquals(listOf(cartEntity), result.first())
    }

    @Test
    fun insertOrUpdateCartRepositoryTest() = runTest {
        whenever(cartDAO.insertCart(cartEntity)).thenReturn(Unit)

        val result = cartRepository.insertOrUpdateItem(cartEntity)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun cekItemCartRepositoryTest() = runTest {
        val productId = ""
        whenever(cartDAO.cekItem(productId)).thenReturn(cartEntity)

        val result = cartRepository.cekItem(productId)
        Assert.assertEquals(cartEntity, result)
    }

    @Test
    fun getItemCartRepositoryTest() = runTest {
        val productId = ""
        whenever(cartDAO.getItem(productId)).thenReturn(flowOf(cartEntity))

        val result = cartRepository.getItem(productId)
        Assert.assertEquals(cartEntity, result.first())
    }

    @Test
    fun deleteCartCartRepositoryTest() = runTest {
        whenever(cartDAO.deleteCart()).thenReturn(Unit)

        val result = cartRepository.deleteCart()
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun updateQuantityCartCartRepositoryTest() = runTest {
        val productId = ""
        val quantity = 1
        whenever(cartDAO.updateQuantityCart(productId, quantity)).thenReturn(Unit)

        val result = cartRepository.updateQuantityCart(productId, quantity)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun updateSelectedCartCartRepositoryTest() = runTest {
        val productId = ""
        val isSelected = true
        whenever(cartDAO.updateSelectedCart(productId, isSelected)).thenReturn(Unit)

        val result = cartRepository.updateSelectedCart(productId, isSelected)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun updateAllSelectedCartCartRepositoryTest() = runTest {
        val isSelected = true
        whenever(cartDAO.updateAllSelectedCart(isSelected)).thenReturn(Unit)

        val result = cartRepository.updateAllSelectedCart(isSelected)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun deleteAllDataCart() = runTest {
        whenever(cartDAO.deleteAllDataCart()).thenReturn(Unit)

        val result = cartRepository.deleteAllDataCart()
        Assert.assertEquals(Unit, result)
    }
}