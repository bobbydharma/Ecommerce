package com.example.ecommerce.ui.main.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.checkout.toChekoutList
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var listViewAdapter : CartAdapter
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

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

        checkAllItem()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItem.collectLatest {
                if (it.isNotEmpty()) {
                    binding.containerCart.isVisible = true
                    binding.containerErorCart.isVisible = false
                    totalPrice(it)
                    setCheckAllItem(it)
                    deleteButton(it)
                    deleteAtOnce(it)
                    buyCart(it)
                    listViewAdapter.submitList(it)
                    logEventViewCart(it)


                } else {
                    listViewAdapter.submitList(it)
                    binding.containerCart.isVisible = false
                    binding.containerErorCart.isVisible = true
                }
            }
        }
    }

    private fun logEventViewCart(it: List<CartEntity>) {
        var totalPrice : Int = 0
        it.forEach {
            if (it.isSelected){
                val productPrice = (it.productPrice + it.variantPrice) * it.quantity
                totalPrice = totalPrice + productPrice
            }

        }

        val item = it.map{
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, it.productId )
                putString(FirebaseAnalytics.Param.ITEM_NAME, it.productName)
                putString(FirebaseAnalytics.Param.ITEM_VARIANT, it.variantName)
                putString(FirebaseAnalytics.Param.ITEM_BRAND, it.brand)
                putDouble(FirebaseAnalytics.Param.PRICE, (it.productPrice+it.variantPrice).toDouble())
            }
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_CART){
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.VALUE, totalPrice.toDouble())
            item?.let { param(FirebaseAnalytics.Param.ITEMS, it.toTypedArray()) }
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
            binding.btnDelateCart.isVisible = true
            binding.btnBuyCart.isEnabled = true
        }else{
            binding.btnDelateCart.isVisible = false
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

    private fun checkAllItem() {
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

        //                start log event
        val itemProduct = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, cartEntity.productId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, cartEntity.productName)
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, cartEntity.variantName)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, cartEntity.brand)
            putDouble(FirebaseAnalytics.Param.PRICE, (cartEntity.productPrice+cartEntity.variantPrice).toDouble())
        }
        val itemProductCart = Bundle(itemProduct).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }
//                end log event

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART){
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(FirebaseAnalytics.Param.VALUE, (cartEntity.productPrice+cartEntity.variantPrice).toDouble())
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemProductCart))
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}