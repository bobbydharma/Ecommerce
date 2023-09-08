package com.example.ecommerce.ui.main.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentDetailProductBinding
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.convertToCheckoutList
import com.example.ecommerce.model.products.mappingCart
import com.example.ecommerce.ui.prelogin.onboarding.ViewPagerImageAdapter
import com.example.ecommerce.utils.Result
import com.google.android.material.chip.Chip
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

@AndroidEntryPoint
class DetailProductFragment : Fragment() {
    private var _binding : FragmentDetailProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailProductViewModel by viewModels()
    private lateinit var viewPagerImageAdapter: ViewPagerImageAdapter
    private var globalIndex = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLihatUlasan.setOnClickListener {
            val bundle = bundleOf("id_product_review" to viewModel.id)
            findNavController().navigate(R.id.action_detailProductFragment3_to_ulasanPembeliFragment2, bundle)
        }

        viewModel.detailProduct.observe(viewLifecycleOwner){result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBarDetailProduct.isVisible = false
                    binding.divider6.isVisible = true
                    binding.scrollView2.isVisible = true
                    binding.linearLayout2.isVisible = true
                    setDisplay(result)
                    saveToCart(result.data.data)
                    saveToWishlist(result.data.data)
                    buyNow(result.data.data)
                    setWIshlist(result.data.data.productId)
                }
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Loading -> {
                    binding.progressBarDetailProduct.isVisible = true
                    binding.scrollView2.isVisible = false
                    binding.divider6.isVisible = false
                    binding.linearLayout2.isVisible = false
                }
                else -> {}
            }
        }
    }

    private fun buyNow(data: DataProductDetail) {
        binding.btnBuyDetailProduct.setOnClickListener {
            val bundle = bundleOf("CheckoutList" to data.convertToCheckoutList(globalIndex))
            findNavController().navigate(R.id.action_detailProductFragment3_to_checkoutFragment, bundle)
        }
    }

    private fun saveToWishlist(data: DataProductDetail) {
        binding.btnToggleWishlist.setOnCheckedChangeListener{_, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                if (isChecked){
                    viewModel.insertToWishlist(data)
                }else{
                    viewModel.deleteWishlist(data)
                }
            }
        }
    }

    private fun setWIshlist(productId: String) {
        viewLifecycleOwner.lifecycleScope.launch{
            val cekItemWishlist = viewModel.cekItemWishlist(productId)

            if (cekItemWishlist != null){
                binding.btnToggleWishlist.isChecked = true
            }else{
                binding.btnToggleWishlist.isChecked = false
            }
        }

    }

    private fun saveToCart(data: DataProductDetail) {

        binding.btnAddCart.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val cartItem = viewModel.cekItem(data.productId)
                if (cartItem != null) {
                    if (cartItem.stock > cartItem.quantity) {
                        viewModel.insertOrUpdateItem(data, globalIndex)
                        val snackBar = Snackbar.make(requireView(), "Ditambahkan Kekeranjang", Snackbar.LENGTH_SHORT)
                        snackBar.setAnchorView(binding.linearLayout2).show()
                    } else {
                        val snackBar = Snackbar.make(requireView(), "Stok Habis", Snackbar.LENGTH_SHORT)
                        snackBar.setAnchorView(binding.linearLayout2)
                        snackBar.setBackgroundTint(android.graphics.Color.RED).show()
                    }
                }else{
                    viewModel.insertOrUpdateItem(data, globalIndex)
                    val snackBar = Snackbar.make(requireView(), "Ditambahkan Kekeranjang", Snackbar.LENGTH_SHORT)
                    snackBar.setAnchorView(binding.linearLayout2).show()
                }
            }
        }
    }

    private fun setDisplay(result: Result.Success<ProductDetailResponse>) {

        result.data.data.apply {
            var totalPrice : Int?
            binding.tvNameProduct.text = productName
            binding.tvPriceProduct.text = productPrice.formatToIDR()
            binding.tvSaleProduct.text = "Terjual ${sale}"
            binding.tvReviewProduct.text = "${productRating} (${totalRating})"
            binding.tvDescriptionProduct.text = description
            binding.tvReviewBottomProduct.text = productRating.toString()
            binding.tvPersentaseReviewProduct.text = "${totalSatisfaction}% pembeli merasa puas"
            binding.tvTotalRattingReviewProduct.text = "${totalRating} rating · ${totalReview} ulasan"

            viewPagerImageAdapter = ViewPagerImageAdapter(image)
            binding.pagerImageProduct.adapter = viewPagerImageAdapter

            TabLayoutMediator(binding.tabLayoutProduct, binding.pagerImageProduct) { _, _ -> }.attach()

            binding.chipGroupDetailProduct.setOnCheckedChangeListener { group, checkedIds ->
                val selectedChip: Chip? = group.findViewById(checkedIds)
                val selectedText = selectedChip?.text.toString()
                productVariant.forEachIndexed{index, it ->
                    if (selectedText == it.variantName){
                        totalPrice = productPrice + it.variantPrice
                        globalIndex = index
                        binding.tvPriceProduct.text = totalPrice?.formatToIDR()
                    }
                }

            }

            productVariant.forEachIndexed {index, it ->
                val chip = Chip(requireContext())
                chip.text = it.variantName
                chip.id = index
                binding.chipGroupDetailProduct.addView(chip)
                chip.isCheckable = true
                if(index == 0)chip.isChecked = true
            }
        }
    }

    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }
}