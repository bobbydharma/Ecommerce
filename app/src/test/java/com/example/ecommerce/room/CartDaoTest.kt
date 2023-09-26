package com.example.ecommerce.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.room.dao.CartDAO
import com.example.ecommerce.room.entity.CartEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var cartDao: CartDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        cartDao = database.cartDAO()
    }

    @After
    fun teardown() {
        database.close()
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
    fun insertAndGetAllCartItems() = runTest {
        cartDao.insertCart(cartEntity)
        val allCartItems = cartDao.getAll().first()
        assertEquals(listOf(cartEntity), allCartItems)
    }

    @Test
    fun cekItem() = runTest {
        val productId = "productId"
        cartDao.insertCart(cartEntity)
        val result = cartDao.cekItem(productId)
        assertEquals(cartEntity, result)
    }

    @Test
    fun getItem() = runTest {
        val productId = "productId"
        cartDao.insertCart(cartEntity)
        val result = cartDao.getItem(productId).first()
        assertEquals(cartEntity, result)
    }

    @Test
    fun deleteCart() = runTest {
        val productId = "productId"
        cartDao.insertCart(cartEntity)
        cartDao.deleteCart(cartEntity)
        val result = cartDao.getItem(productId).first()
        assertEquals(result, null)
    }

    @Test
    fun updateQuantityCart() = runTest {
        val productId = "productId"
        val quantityUpdate = 2
        cartDao.insertCart(cartEntity)
        cartDao.updateQuantityCart(productId,quantityUpdate)
        val result = cartDao.getItem(productId).first()
        assertEquals(quantityUpdate, result?.quantity)
    }

    @Test
    fun updateSelectedCart() = runTest {
        val productId = "productId"
        val selectedUpdate = true
        cartDao.insertCart(cartEntity)
        cartDao.updateSelectedCart(productId,selectedUpdate)
        val result = cartDao.getItem(productId).first()
        assertEquals(selectedUpdate, result?.isSelected)
    }

    @Test
    fun updateAllSelectedCart() = runTest {
        val productId = "productId"
        val selectedUpdate = true
        cartDao.insertCart(cartEntity)
        cartDao.updateAllSelectedCart(selectedUpdate)
        val result = cartDao.getItem(productId).first()
        assertEquals(selectedUpdate, result?.isSelected)
    }

    @Test
    fun deleteAllDataCart() = runTest {
        cartDao.insertCart(cartEntity)
        cartDao.deleteAllDataCart()
        val allCartItems = cartDao.getAll().first()
        assertEquals(0, allCartItems.size)
    }
}