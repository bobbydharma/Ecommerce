package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ecommerce.MainViewModel
import com.example.ecommerce.auth.CekAuthorization
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
class MainActivityViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var cekAuthorization: CekAuthorization
    private lateinit var mainActivityViewModel: MainViewModel

    val expected = MutableLiveData(true)

    @Before
    fun setup() {
        cekAuthorization = Mockito.mock()
        whenever(cekAuthorization.isDataLoaded).thenReturn(expected)
        mainActivityViewModel = MainViewModel(cekAuthorization)
    }

    @Test
    fun `cek authorization main activity viewmodel test`() = runTest {
        val result = mainActivityViewModel.test
        Assert.assertEquals(expected, result)
    }

}