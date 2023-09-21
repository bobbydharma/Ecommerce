package com.example.ecommerce.ui.main.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentPaymentBinding
import com.example.ecommerce.utils.Result
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding : FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PaymentViewModel>()
    private lateinit var parentPaymentAdapter: ParentPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        parentPaymentAdapter = ParentPaymentAdapter {
            requireActivity().supportFragmentManager.setFragmentResult(
                "itemPayment",
                bundleOf("itemPayment" to it)
            )
            findNavController().navigateUp()
        }
        binding.rvPayment.adapter = parentPaymentAdapter

//        viewModel.postPayment()


//        viewModel.paymentItem.observe(viewLifecycleOwner){ result ->
//            when (result) {
//                is Result.Success -> {
//                    binding.rvPayment.isVisible = true
//                    binding.progressBarPayment.isVisible = false
//                    binding.errorConnection.isVisible = false
//                    parentPaymentAdapter.submitList(result.data.data)
//                }
//
//                is Result.Error -> {
//                    binding.rvPayment.isVisible = false
//                    binding.progressBarPayment.isVisible = false
//                    binding.errorConnection.isVisible = true
//                }
//
//                is Result.Loading -> {
//                    binding.rvPayment.isVisible = false
//                    binding.progressBarPayment.isVisible = true
//                    binding.errorConnection.isVisible = false
//                }
//
//                else -> {}
//            }
//        }

        viewModel.stringPayment.observe(viewLifecycleOwner){result ->

            when(result){
                is Result.Success -> {
                    binding.rvPayment.isVisible = true
                    binding.progressBarPayment.isVisible = false
                    binding.errorConnection.isVisible = false
                    val gson = Gson()
                    val data = gson.fromJson(result.data, PaymentResponse::class.java)
                    parentPaymentAdapter.submitList(data.data)
                }

                is Result.Error -> {
                    binding.rvPayment.isVisible = false
                    binding.progressBarPayment.isVisible = false
                    binding.errorConnection.isVisible = true
                }

                is Result.Loading -> {
                    binding.rvPayment.isVisible = false
                    binding.progressBarPayment.isVisible = true
                    binding.errorConnection.isVisible = false
                }

                else -> {}
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}