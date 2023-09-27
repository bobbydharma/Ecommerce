package com.example.ecommerce.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.repository.MainRepository
import com.example.ecommerce.ui.main.payment.PaymentViewModel
import com.example.ecommerce.utils.Result
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class PaymentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MainRepository
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var paymentViewModel: PaymentViewModel

    @Before
    fun setup(){
        repository = Mockito.mock()
        remoteConfig = Mockito.mock()
        paymentViewModel = PaymentViewModel(repository,remoteConfig)
    }

    @Test
    fun `fetch activate remote config payment viewmodel test`() = runTest {
//        whenever(remoteConfig.fetchAndActivate()).thenReturn(Unit)
//
//        val result = paymentViewModel
    }
}