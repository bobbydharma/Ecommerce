package com.example.ecommerce.ui.main.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingSource
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentTransactionBinding
import com.example.ecommerce.utils.Result
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding : FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TransactionViewModel>()
    private lateinit var transactionAdapter: TransactionAdapter
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionAdapter = TransactionAdapter {
            val bundle = bundleOf("FulfillmentResponse" to it.convertToDataFulfillment(), "SourceFragment" to "Transaction")
            val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
            navController.navigate(R.id.main_to_send_review, bundle)
            firebaseAnalytics.logEvent("BUTTON_CLICK"){
                param("BUTTON_NAME", "Transaction_Review" )
            }
        }

        binding.apply {
            rvTransaction.adapter = transactionAdapter
            rvTransaction.itemAnimator?.changeDuration = 0

            btnRefreshConnection.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getTransaction()
                }
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getTransaction()
            viewModel.transaction.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data.data.isNotEmpty()){
                            binding.containerTransaction.isVisible = true
                            binding.containerErorTransaction.isVisible = false
                            binding.errorConnection.isVisible = false
                            binding.progressBarTransaction.isVisible = false
                            transactionAdapter.submitList(result.data.data)
                        }
                    }
                    is Result.Error -> {
                        when(result.exception){
                            is HttpException -> {
                                binding.apply {
                                    containerTransaction.isVisible = false
                                    errorConnection.isVisible = false
                                    progressBarTransaction.isVisible = false
                                    containerErorTransaction.isVisible = true
                                }
                            }
                            is IOException -> {
                                binding.apply {
                                    containerTransaction.isVisible = false
                                    errorConnection.isVisible = true
                                    progressBarTransaction.isVisible = false
                                    containerErorTransaction.isVisible = false
                                }

                            }
                            else -> {
                                Log.d("else", "${result.exception}")
                            }
                        }
                    }
                    is Result.Loading -> {
                        binding.apply {
                            containerTransaction.isVisible = false
                            errorConnection.isVisible = false
                            containerErorTransaction.isVisible = false
                            progressBarTransaction.isVisible = true
                        }
                    }
                    else -> {}
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}