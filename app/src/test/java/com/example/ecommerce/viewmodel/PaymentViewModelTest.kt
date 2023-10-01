//package com.example.ecommerce.viewmodel
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import com.example.ecommerce.repository.MainRepository
//import com.example.ecommerce.ui.main.payment.PaymentViewModel
//import com.example.ecommerce.utils.Result
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.TaskCompletionSource
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.Mockito
//import org.mockito.invocation.InvocationOnMock
//import org.mockito.kotlin.argumentCaptor
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.verify
//import org.mockito.kotlin.whenever
//import org.mockito.stubbing.Answer
//import java.util.concurrent.CompletableFuture
//
//@RunWith(JUnit4::class)
//class PaymentViewModelTest {
//
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @get:Rule
//    var mainDispatcherRule = MainDispatcherRule()
//
//    private lateinit var repository: MainRepository
//    private lateinit var remoteConfig: FirebaseRemoteConfig
//    private lateinit var paymentViewModel: PaymentViewModel
//
//    val expectedResult = "Expected Remote Config Data"
//    @Before
//    fun setup(){
//        repository = Mockito.mock()
//        remoteConfig = Mockito.mock()
//
//        whenever(remoteConfig.fetchAndActivate()).thenAnswer {
//            val onCompleteListener = it.arguments[0] as OnCompleteListener<Void>
//            onCompleteListener.onComplete(mockTask(true))
//            null
//        }
//        whenever(remoteConfig.getString("Payment")).thenReturn(expectedResult)
//
//        paymentViewModel = PaymentViewModel(repository,remoteConfig)
//    }
//
//    @Test
//    fun `fetch activate remote config payment viewmodel test`() = runTest {
//
//        whenever(remoteConfig.fetchAndActivate()).thenAnswer {
//            val onCompleteListener = it.arguments[0] as OnCompleteListener<Void>
//            onCompleteListener.onComplete(mockTask(true))
//            null
//        }
//        whenever(remoteConfig.getString("Payment")).thenReturn(expectedResult)
//
//        paymentViewModel.fetchAndActivateRemoteConfig()
//
//        val result = paymentViewModel.stringPayment.value
//        assert(result is Result.Success)
//        assert((result as Result.Success).data == expectedResult)
//    }
//
//    private fun mockTask(isSuccessful: Boolean) = if (isSuccessful) {
//        val task = Mockito.mock(Task::class.java)
//        whenever(task.isSuccessful).thenReturn(true)
//        task as Task<Void>
//    } else {
//        val task = Mockito.mock(Task::class.java)
//        whenever(task.isSuccessful).thenReturn(false)
//        task as Task<Void>
//    }
//}