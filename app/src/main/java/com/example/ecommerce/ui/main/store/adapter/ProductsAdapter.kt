package com.example.ecommerce.ui.main.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ItemGridBinding
import com.example.ecommerce.databinding.ItemListBinding
import com.example.ecommerce.model.products.Items
import java.text.NumberFormat

class ProductsAdapter(diffCallback: DiffUtil.ItemCallback<Items>, private val onItemClick: (String) -> Unit) :
    PagingDataAdapter<Items, ViewHolder>  (diffCallback) {

    var isGridMode = false
    companion object {
        private const val LIST_PRODUCT = 0
        private const val GRID_PRODUCT = 1
    }

    inner class GridProductsViewHolder(private val binding: ItemGridBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind (items: Items){

                Glide.with(itemView.context)
                    .load(items.image)
                    .into(binding.ivProductGrid)

                binding.tvProductNameGrid.text = items.productName
                val format = items.productPrice.formatToIDR()
                binding.tvProductPriceGrid.text = format
                binding.tvStoreGrid.text = items.store
                binding.tvSaleGrid.text = "${items.productRating} | Terjual ${items.sale}"

            }
        }

    inner class ListProductsViewHolder(private val binding: ItemListBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(items: Items) {
            Glide.with(itemView.context)
                .load(items.image)
                .into(binding.ivProductList)

            binding.tvProductNameList.text = items.productName
            val format = items.productPrice.formatToIDR()
            binding.tvProductPriceList.text = format
            binding.tvStoreList.text = items.store
            binding.tvSaleList.text = "${items.productRating} | Terjual ${items.sale}"
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridMode) GRID_PRODUCT else LIST_PRODUCT
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            LIST_PRODUCT -> {
                val binding = ItemListBinding.inflate(inflater, parent, false)
                ListProductsViewHolder(binding)
            }
            GRID_PRODUCT -> {
                val binding = ItemGridBinding.inflate(inflater, parent, false)
                GridProductsViewHolder(binding)
            }
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ListProductsViewHolder -> {
                if (item != null){
                 holder.bind(item)
                }
            }
            is GridProductsViewHolder -> {
                if (item != null){
                    holder.bind(item)
                }
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }

        holder.itemView.setOnClickListener {
            getItem(position)?.let { it1 -> onItemClick(it1.productId) }
        }
    }


    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }

    object ProductComparator : DiffUtil.ItemCallback<Items>() {
        override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
            // Id is unique.
            return oldItem.productId  == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
            return oldItem == newItem
        }
    }
}
