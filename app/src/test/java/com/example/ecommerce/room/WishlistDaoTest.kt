package com.example.ecommerce.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.room.dao.WishlistDAO
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WishlistDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var wishlistDAO: WishlistDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        wishlistDAO = database.wishlistDAO()
    }

    @After
    fun teardown() {
        database.close()
    }

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

    @Test
    fun insertAndGetAllWishlistItems() = runTest {
        wishlistDAO.insertWishlist(wishlistEntity)
        val allWishlistItems = wishlistDAO.getAll().first()
        assertEquals(listOf(wishlistEntity), allWishlistItems)
    }

    @Test
    fun cekItemWishlist() = runTest {
        val productId = "productId"
        wishlistDAO.insertWishlist(wishlistEntity)
        val allWishlistItems = wishlistDAO.cekItemWishlist(productId).first()
        assertEquals(wishlistEntity, allWishlistItems)
    }

    @Test
    fun deleteWishlist() = runTest {
        wishlistDAO.insertWishlist(wishlistEntity)
        wishlistDAO.deleteWishlist(wishlistEntity)
        val result = wishlistDAO.getAll().first()
        assertEquals(result.size, 0)
    }

    @Test
    fun deleteAllDataWishlist() = runTest {
        wishlistDAO.insertWishlist(wishlistEntity)
        wishlistDAO.deleteAllDataWishlist()
        val allCartItems = wishlistDAO.getAll().first()
        assertEquals(0, allCartItems.size)
    }

}