package com.example.ecommerce.ui.main.wishlist

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.room.entity.WishlistEntity
import com.example.ecommerce.core.room.entity.convertToDetail
import com.example.ecommerce.databinding.FragmentWishlistBinding
import com.example.ecommerce.ui.main.cart.WishlistAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WishlistViewModel>()
    private lateinit var wishlistAdapter: WishlistAdapter

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishlistAdapter = WishlistAdapter(
            WishlistAdapter.WishlistEntityDiffCallback,
            { wishlistEntity -> deleteItemClick(wishlistEntity) },
            { wishlistEntity -> addItemClick(wishlistEntity) },
            {wishlistEntity -> onitemclick(wishlistEntity) }
        )

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.apply {
            rvWishlist.adapter = wishlistAdapter

            btnToggle.setOnCheckedChangeListener { _, isChecked ->

                wishlistAdapter.isGridMode = isChecked
                setLayoutManager(isChecked)
                wishlistAdapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wishlistItem.collectLatest {
                if (it.isNotEmpty()) {
                    binding.containerErorWishlist.isVisible = false
                    binding.containerWishlist.isVisible = true
                    wishlistAdapter.submitList(it)
                    binding.tvTotalWishlist.text =
                        getString(R.string.total_barang, it.size.toString())
                } else {
                    binding.containerErorWishlist.isVisible = true
                    binding.containerWishlist.isVisible = false
                }

            }
        }

    }

    private fun onitemclick(wishlistEntity: WishlistEntity) {
        val bundle = bundleOf("id_product" to wishlistEntity.productId)
        val navController =
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        navController.navigate(R.id.main_to_detail_product, bundle)
    }

    private fun addItemClick(wishlistEntity: WishlistEntity) {

//                start log event
        val itemProduct = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, wishlistEntity.productId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, wishlistEntity.productName)
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, wishlistEntity.varianName)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, wishlistEntity.brand)
            putDouble(
                FirebaseAnalytics.Param.PRICE,
                (wishlistEntity.productPrice + wishlistEntity.varianPrice).toDouble()
            )
        }
        val itemProductCart = Bundle(itemProduct).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }
//                end log event

        viewLifecycleOwner.lifecycleScope.launch {
            val cartItem = viewModel.cekItem(wishlistEntity.productId)
            if (cartItem != null) {
                if (cartItem.stock > cartItem.quantity) {
                    viewModel.insertOrUpdateItem(wishlistEntity.convertToDetail(), 0)
                    val snackBar = Snackbar.make(
                        requireView(),
                        getString(R.string.ditambahkan_kekeranjang), Snackbar.LENGTH_SHORT
                    )
                    snackBar.setAnchorView(R.id.bnv_child).show()

//                start log event
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
                        param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                        param(
                            FirebaseAnalytics.Param.VALUE,
                            (cartItem.productPrice + cartItem.variantPrice).toDouble()
                        )
                        param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemProductCart))
                    }
//                 end log event

                } else {
                    val snackBar = Snackbar.make(
                        requireView(),
                        getString(R.string.stok_habis), Snackbar.LENGTH_SHORT
                    )
                    snackBar.setAnchorView(R.id.bnv_child)
                    snackBar.setBackgroundTint(Color.RED).show()
                }
            } else {
                viewModel.insertOrUpdateItem(wishlistEntity.convertToDetail(), 0)
                val snackBar = Snackbar.make(
                    requireView(),
                    getString(R.string.ditambahkan_kekeranjang),
                    Snackbar.LENGTH_SHORT
                )
                snackBar.setAnchorView(R.id.bnv_child).show()

//                start log event
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
                    param(FirebaseAnalytics.Param.CURRENCY, "IDR")
                    param(
                        FirebaseAnalytics.Param.VALUE,
                        (wishlistEntity.productPrice + wishlistEntity.varianPrice).toDouble()
                    )
                    param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemProductCart))
                }
//                end log event


            }
        }

        firebaseAnalytics.logEvent("BUTTON_CLICK") {
            param("BUTTON_NAME", "Wishlist_AddToCart")
        }

    }

    private fun deleteItemClick(wishlistEntity: WishlistEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteWishlist(wishlistEntity)
        }
        firebaseAnalytics.logEvent("BUTTON_CLICK") {
            param("BUTTON_NAME", "Wishlist_Delete")
        }
    }

    private fun setLayoutManager(checked: Boolean) {
        val firstVisibleItemPosition =
            (binding.rvWishlist.layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0
        if (checked) {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvWishlist.layoutManager = layoutManager
            binding.rvWishlist.layoutManager?.scrollToPosition(firstVisibleItemPosition)

        } else {
            binding.rvWishlist.layoutManager = GridLayoutManager(requireContext(), 1)
            binding.rvWishlist.layoutManager?.scrollToPosition(firstVisibleItemPosition)
        }
    }

}