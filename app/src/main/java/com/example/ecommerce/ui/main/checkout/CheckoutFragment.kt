package com.example.ecommerce.ui.main.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCheckoutBinding
import com.example.ecommerce.databinding.FragmentTransactionBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.cart.CartAdapter
import com.example.ecommerce.ui.main.detail.ReviewViewModel
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemCheckoutList.collectLatest {
                checkoutAdapter.submitList(it?.item)
                Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun onAddItemClick(checkoutItem: CheckoutItem) {
        if (checkoutItem.stock > checkoutItem.quantity){
            viewModel.itemCheckoutList.value?.item?.forEachIndexed { index, item ->
                if (checkoutItem.productId == item.productId){
                    viewModel.itemCheckoutList.value?.item?.get(index)?.quantity = item.quantity+1
                    Toast.makeText(context, "${viewModel.itemCheckoutList.value?.item?.get(index)?.quantity}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onMinItemClick(checkoutItem: CheckoutItem) {

    }

}