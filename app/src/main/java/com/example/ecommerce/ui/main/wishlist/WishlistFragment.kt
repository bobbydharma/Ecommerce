package com.example.ecommerce.ui.main.wishlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentWishlistBinding
import com.example.ecommerce.room.entity.WishlistEntity
import com.example.ecommerce.room.entity.convertToDetail
import com.example.ecommerce.ui.main.cart.CartAdapter
import com.example.ecommerce.ui.main.cart.WishlistAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding : FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WishlistViewModel>()
    private lateinit var wishlistAdapter: WishlistAdapter

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
            {wishlistEntity -> deleteItemClick(wishlistEntity)},
            {wishlistEntity -> addItemClick(wishlistEntity) }
            )

        binding.rvWishlist.adapter = wishlistAdapter

        binding.btnToggle.setOnCheckedChangeListener{ _ , isChecked ->

            wishlistAdapter.isGridMode = isChecked
            setLayoutManager(isChecked)
            wishlistAdapter.notifyDataSetChanged()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wishlistItem.collectLatest {
                if (it.isNotEmpty()){
                    binding.containerErorWishlist.isVisible=false
                    binding.containerWishlist.isVisible = true
                    wishlistAdapter.submitList(it)
                    binding.tvTotalWishlist.text = "${it.size} ${R.string.barang}"
                }else{
                    binding.containerErorWishlist.isVisible = true
                    binding.containerWishlist.isVisible = false
                }

            }
        }

    }

    private fun addItemClick(wishlistEntity: WishlistEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            val cartItem = viewModel.cekItem(wishlistEntity.productId)
            if (cartItem != null) {
                if (cartItem.stock > cartItem.quantity) {
                    viewModel.insertOrUpdateItem(wishlistEntity.convertToDetail(), 0)
                    val snackBar = Snackbar.make(requireView(),
                        getString(R.string.ditambahkan_kekeranjang), Snackbar.LENGTH_SHORT)
                    snackBar.setAnchorView(R.id.bnv_child).show()
                } else {
                    val snackBar = Snackbar.make(requireView(),
                        getString(R.string.stok_habis), Snackbar.LENGTH_SHORT)
                    snackBar.setAnchorView(R.id.bnv_child)
                    snackBar.setBackgroundTint(Color.RED).show()
                }
            }else{
                viewModel.insertOrUpdateItem(wishlistEntity.convertToDetail(), 0)
                val snackBar = Snackbar.make(requireView(), getString(R.string.ditambahkan_kekeranjang), Snackbar.LENGTH_SHORT)
                snackBar.setAnchorView(R.id.bnv_child).show()
            }
        }
    }

    private fun deleteItemClick(wishlistEntity: WishlistEntity) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteWishlist(wishlistEntity)
        }
    }

    private fun setLayoutManager(checked: Boolean) {
        val firstVisibleItemPosition = (binding.rvWishlist.layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition() ?: 0
        if (checked){
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvWishlist.layoutManager = layoutManager
            binding.rvWishlist.layoutManager?.scrollToPosition(firstVisibleItemPosition)

        }else{
            binding.rvWishlist.layoutManager = GridLayoutManager(requireContext(), 1)
            binding.rvWishlist.layoutManager?.scrollToPosition(firstVisibleItemPosition)
        }
    }

}