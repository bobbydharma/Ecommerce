package com.example.ecommerce.ui.main.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCheckoutBinding
import com.example.ecommerce.ui.main.payment.Item
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.formatToIDR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding : FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CheckoutViewModel>()
    private lateinit var checkoutAdapter: CheckoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.setFragmentResultListener("itemPayment", viewLifecycleOwner) { requestKey, bundle ->
            viewModel.dataItemPayment = bundle.getParcelable("itemPayment")!!
            var dataFulfillment : FulfillmentRequest
            if (viewModel.dataItemPayment.label.isNotEmpty()) {
                Glide.with(view.context)
                    .load(viewModel.dataItemPayment.image)
                    .into(binding.ivItemPaymentCheckout)
                binding.tvLabelPaymentCheckout.text = viewModel.dataItemPayment.label
                binding.btnPayCheckout.isEnabled = true
            }
            binding.btnPayCheckout.setOnClickListener {
                val items = viewModel.itemCheckoutList.value?.itemCheckout?.toFulfillmentItem()
                if (viewModel.dataItemPayment.label.isNotEmpty()) {
                    if (items != null) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            dataFulfillment = FulfillmentRequest(viewModel.dataItemPayment.label, items)
                            viewModel.postFulfillment(dataFulfillment)
                        }
                    }
                }
            }
        }

        binding.btnPayCheckout.isEnabled = false
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        checkoutAdapter = CheckoutAdapter(
            CheckoutAdapter.checkoutItemDiffCallback,
            { checkoutItem -> onAddItemClick(checkoutItem) },
            { checkoutItem -> onMinItemClick(checkoutItem) }
        )

        binding.rvProductCheckout.adapter = checkoutAdapter
        setTotalPrice(viewModel.itemCheckoutList)

        binding.carfViewCheckout.setOnClickListener{
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentFragment)
        }

        checkoutAdapter.submitList(viewModel.itemCheckoutList.value?.itemCheckout)

        viewModel.fulfillment.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val bundle = bundleOf("FulfillmentResponse" to result.data.data , "SourceFragment" to "Checkout")
                    findNavController().navigate(R.id.action_checkoutFragment_to_sendReviewFragment, bundle)
                }
                is Result.Error -> {
                    Log.d("error", result.toString())
                }
                is Result.Loading -> {
                }
                else -> {
                }
            }
        }

    }

    private fun setTotalPrice(item: StateFlow<CheckoutList?>) {
        var totalPrice = 0
        item.value?.itemCheckout?.forEachIndexed { index, checkoutItem ->
            val priceProduct = (checkoutItem.productPrice + checkoutItem.varianPrice) * checkoutItem.quantity
            totalPrice += priceProduct
        }
        binding.tvTotalPriceCheckout.text = totalPrice.formatToIDR()
    }

    private fun onAddItemClick(checkoutItem: CheckoutItem) {
        setTotalPrice(viewModel.itemCheckoutList)
    }

    private fun onMinItemClick(checkoutItem: CheckoutItem) {
        setTotalPrice(viewModel.itemCheckoutList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.fulfillment.removeObservers(viewLifecycleOwner)
    }

    override fun onPause() {
        super.onPause()
        viewModel.fulfillment.removeObservers(viewLifecycleOwner)
    }

}