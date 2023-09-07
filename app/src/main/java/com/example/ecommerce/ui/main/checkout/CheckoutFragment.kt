package com.example.ecommerce.ui.main.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

        checkoutAdapter = CheckoutAdapter(
            CheckoutAdapter.checkoutItemDiffCallback,
            { checkoutItem -> onAddItemClick(checkoutItem) },
            { checkoutItem -> onMinItemClick(checkoutItem) }
        )

        binding.rvProductCheckout.adapter = checkoutAdapter

        binding.carfViewCheckout.setOnClickListener{
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemCheckoutList.collectLatest {
                checkoutAdapter.submitList(it?.item)
                setTotalPrice(it?.item)
            }
        }



    }

    private fun setTotalPrice(item: List<CheckoutItem>?) {
        var totalPrice = 0
        item?.forEachIndexed { index, checkoutItem ->
            val priceProduct = (checkoutItem.productPrice + checkoutItem.varianPrice) * checkoutItem.quantity
            totalPrice += priceProduct
        }
        binding.tvTotalPriceCheckout.text = totalPrice.toString()
    }

    private fun onAddItemClick(checkoutItem: CheckoutItem) {

    }

    private fun onMinItemClick(checkoutItem: CheckoutItem) {
    }

}