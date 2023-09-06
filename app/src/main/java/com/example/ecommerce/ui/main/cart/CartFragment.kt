package com.example.ecommerce.ui.main.cart

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
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.checkout.toChekoutList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var listViewAdapter : CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        listViewAdapter = CartAdapter(
            CartAdapter.CartEntityDiffCallback,
            { cartEntity -> onDeleteItemClick(cartEntity) },
            { cartEntity -> onAddItemClick(cartEntity) },
            { cartEntity -> onMinItemClick(cartEntity) },
            {cartEntity -> onSelectedItem(cartEntity) }
        )

        binding.lvCart.adapter = listViewAdapter
        binding.lvCart.apply {
            itemAnimator?.changeDuration = 0
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItem.collectLatest {
                if (it.isNotEmpty()) {
                    binding.containerCart.isVisible = true
                    binding.containerErorCart.isVisible = false
                    totalPrice(it)
                    checkAllItem(it)
                    setCheckAllItem(it)
                    deleteButton(it)
                    deleteAtOnce(it)
                    buyCart(it)
                    listViewAdapter.submitList(it)
                } else {
                    listViewAdapter.submitList(it)
                    binding.containerCart.isVisible = false
                    binding.containerErorCart.isVisible = true
                }
            }
        }
    }

    private fun buyCart(it: List<CartEntity>) {
        val item = it.filter { it.isSelected ==true }
        val checkoutItem = item.toChekoutList()
        binding.btnBuyCart.setOnClickListener {
            val bundle = bundleOf("CheckoutList" to checkoutItem)
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment, bundle)
        }
    }

    private fun deleteAtOnce(cartEntity: List<CartEntity>) {

        binding.btnDelateCart.setOnClickListener {
            val item = cartEntity.filter { item ->
                item.isSelected
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.deleteCart(*item.toTypedArray())
            }
        }
    }

    private fun deleteButton(it: List<CartEntity>) {
        val isSelected = it.any { it.isSelected == true }

        if (isSelected) {
            binding.btnDelateCart.isEnabled = true
            binding.btnBuyCart.isEnabled = true
        }else{
            binding.btnDelateCart.isEnabled = false
            binding.btnBuyCart.isEnabled = false
        }
    }

    private fun setCheckAllItem(it: List<CartEntity>) {
        binding.checkboxAllItemCart.isChecked = false
        if(it != null){
            val allSelected = it.all { it.isSelected }
            if (allSelected) {
                binding.checkboxAllItemCart.isChecked = allSelected

            }else{
                binding.checkboxAllItemCart.isChecked = allSelected

            }
        }


    }

    private fun checkAllItem(cartEntity: List<CartEntity>) {
        binding.checkboxAllItemCart.setOnClickListener {
            if (binding.checkboxAllItemCart.isChecked){

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateAllSelectedCart(binding.checkboxAllItemCart.isChecked)
                }
            }else{
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateAllSelectedCart(binding.checkboxAllItemCart.isChecked)
                }
            }
        }
    }

    private fun onSelectedItem(cartEntity: CartEntity) {
        var selected : Boolean
        selected = cartEntity.isSelected
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateSelectedCart(cartEntity.productId, !selected)
        }

    }

    private fun totalPrice(it: List<CartEntity>) {
        var totalPrice : Int = 0
        it.forEach {
            if (it.isSelected){
                val productPrice = (it.productPrice + it.variantPrice) * it.quantity
                totalPrice = totalPrice + productPrice
            }

        }
        binding.tvTotalPriceCart.text = totalPrice.formatToIDR()
    }

    private fun onAddItemClick(cartEntity: CartEntity) {
        if (cartEntity.stock > cartEntity.quantity){
            viewLifecycleOwner.lifecycleScope.launch{
                viewModel.updateQuantityCart(cartEntity.productId, cartEntity.quantity+1)
            }
        }

    }

    private fun onDeleteItemClick(cartEntity: CartEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteCart(cartEntity)
        }
    }

    private fun onMinItemClick(cartEntity: CartEntity) {
        if (cartEntity.quantity > 1){
            viewLifecycleOwner.lifecycleScope.launch{
                viewModel.updateQuantityCart(cartEntity.productId, cartEntity.quantity-1)
            }
        }

    }

    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }
}