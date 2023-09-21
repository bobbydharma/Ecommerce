package com.example.ecommerce.ui.main.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemGridBinding
import com.example.ecommerce.databinding.ItemGridWishlistBinding
import com.example.ecommerce.databinding.ItemListBinding
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.databinding.ItemListWishlistBinding
import com.example.ecommerce.room.entity.WishlistEntity
import com.example.ecommerce.ui.main.store.adapter.ProductsAdapter
import java.text.NumberFormat

class WishlistAdapter(
    diffCallback: DiffUtil.ItemCallback<WishlistEntity>,
    private val deleteItemClick: (WishlistEntity) -> Unit,
    private val addItemClick: (WishlistEntity)-> Unit,
) : ListAdapter<WishlistEntity, ViewHolder>(WishlistEntityDiffCallback) {

    var isGridMode = false
    companion object {
        private const val LIST_PRODUCT = 0
        private const val GRID_PRODUCT = 1
    }

    inner class wishlistListViewHolder(private val binding: ItemListWishlistBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(wishlistEntity: WishlistEntity) {

            Glide.with(itemView.context)
                .load(wishlistEntity.image)
                .into(binding.ivProductList)

            binding.tvProductNameList.text = wishlistEntity.productName
            binding.tvProductPriceList.text = (wishlistEntity.productPrice + wishlistEntity.varianPrice).formatToIDR()
            binding.tvStoreList.text = wishlistEntity.store
            binding.tvSaleList.text = itemView.context.getString(R.string.terjuall, wishlistEntity.productRating.toString(), wishlistEntity.sale.toString())

            binding.btnDeleteWishlist.setOnClickListener {
                deleteItemClick(wishlistEntity)
            }

            binding.btnAddWishlist.setOnClickListener {
                addItemClick(wishlistEntity)
            }

        }
    }

    inner class wishlistGridViewHolder(private val binding: ItemGridWishlistBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(wishlistEntity: WishlistEntity) {

            Glide.with(itemView.context)
                .load(wishlistEntity.image)
                .into(binding.ivProductGrid)

            binding.tvProductNameGrid.text = wishlistEntity.productName
            binding.tvProductPriceGrid.text = (wishlistEntity.productPrice + wishlistEntity.varianPrice).formatToIDR()
            binding.tvStoreGrid.text = wishlistEntity.store
            binding.tvSaleGrid.text = "${wishlistEntity.productRating} ${R.string.terjual_item} ${wishlistEntity.sale}"

            binding.btnDeleteWishlist.setOnClickListener {
                deleteItemClick(wishlistEntity)
            }

            binding.btnAddWishlist.setOnClickListener {
                addItemClick(wishlistEntity)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridMode) WishlistAdapter.GRID_PRODUCT else WishlistAdapter.LIST_PRODUCT
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            WishlistAdapter.LIST_PRODUCT -> {
                val binding = ItemListWishlistBinding.inflate(inflater, parent, false)
                wishlistListViewHolder(binding)
            }
            WishlistAdapter.GRID_PRODUCT -> {
                val binding = ItemGridWishlistBinding.inflate(inflater, parent, false)
                wishlistGridViewHolder(binding)
            }
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is WishlistAdapter.wishlistListViewHolder -> {
                if (item != null){
                    holder.bind(item)
                }
            }
            is WishlistAdapter.wishlistGridViewHolder -> {
                if (item != null){
                    holder.bind(item)
                }
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }

    }

    object WishlistEntityDiffCallback : DiffUtil.ItemCallback<WishlistEntity>() {
        override fun areItemsTheSame(oldItem: WishlistEntity, newItem: WishlistEntity): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: WishlistEntity, newItem: WishlistEntity): Boolean {
            return oldItem == newItem
        }
    }

    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }

}