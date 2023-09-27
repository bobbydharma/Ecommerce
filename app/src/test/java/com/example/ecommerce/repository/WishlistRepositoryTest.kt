package com.example.ecommerce.repository

import com.example.ecommerce.room.dao.NotificationDAO
import com.example.ecommerce.room.dao.WishlistDAO
import com.example.ecommerce.room.entity.WishlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class WishlistRepositoryTest {
    private lateinit var wishlistDAO: WishlistDAO
    private lateinit var wishlistRepository: WishlistRepository

    @Before
    fun setup(){
        wishlistDAO = Mockito.mock()
        wishlistRepository = WishlistRepository(wishlistDAO)
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
    fun getAllWishlistRepositoryTest() = runTest {
        whenever(wishlistDAO.getAll()).thenReturn(flowOf(listOf(wishlistEntity)))
        val result = wishlistRepository.getAll()
        Assert.assertEquals(listOf(wishlistEntity), result.first())
    }

    @Test
    fun insertToWishlistWishlistRepositoryTest() = runTest {
        whenever(wishlistDAO.insertWishlist(wishlistEntity)).thenReturn(Unit)

        val result = wishlistRepository.insertToWishlist(wishlistEntity)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun deleteWishlistWishlistRepositoryTest() = runTest {
        whenever(wishlistDAO.deleteWishlist(wishlistEntity)).thenReturn(Unit)

        val result = wishlistRepository.deleteWishlist(wishlistEntity)
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun cekItemWishlistWishlistRepositoryTest() = runTest {
        val productId = ""
        whenever(wishlistDAO.cekItemWishlist(productId)).thenReturn(flowOf(wishlistEntity))

        val result = wishlistRepository.cekItemWishlist(productId)
        Assert.assertEquals(wishlistEntity, result.first())
    }
}