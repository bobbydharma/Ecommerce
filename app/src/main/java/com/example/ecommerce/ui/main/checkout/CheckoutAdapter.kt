package com.example.ecommerce.ui.main.checkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemCheckoutBinding
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.cart.CartAdapter
import java.text.NumberFormat

class CheckoutAdapter(diffCallback: DiffUtil.ItemCallback<CheckoutItem>,
                      private val addItemClick: (CheckoutItem)-> Unit,
                      private val minItemClick: (CheckoutItem) -> Unit
) : androidx.recyclerview.widget.ListAdapter<CheckoutItem, CheckoutAdapter.CheckoutViewHolder>(CheckoutAdapter.checkoutItemDiffCallback) {

    inner class CheckoutViewHolder(private val binding: ItemCheckoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chekoutItem: CheckoutItem) {

            Glide.with(itemView.context)
                .load(chekoutItem.image)
                .into(binding.ivCheckout)

            binding.tvNameProductCheckout.text = chekoutItem.productName
            binding.tvVarianNameCheckout.text = chekoutItem.varianName
            binding.tvStokCheckout.text = "${chekoutItem.stock} Stok"
            binding.tvPriceProductCheckout.text = chekoutItem.productPrice.formatToIDR()
            binding.tvQuantityCheckout.text = chekoutItem.quantity.toString()

            binding.btnPlusCheckout.setOnClickListener {
                if(chekoutItem.stock > chekoutItem.quantity){
                    chekoutItem.quantity += 1
                    binding.tvQuantityCheckout.text = chekoutItem.quantity.toString()
                }
                addItemClick(chekoutItem)
            }

            binding.btnMinCheckout.setOnClickListener {
                if(chekoutItem.quantity > 1){
                    chekoutItem.quantity -= 1
                    binding.tvQuantityCheckout.text = chekoutItem.quantity.toString()
                }
                minItemClick(chekoutItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutAdapter.CheckoutViewHolder {
        val binding =
            ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutAdapter.CheckoutViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

    }

    object checkoutItemDiffCallback : DiffUtil.ItemCallback<CheckoutItem>() {
        override fun areItemsTheSame(oldItem: CheckoutItem, newItem: CheckoutItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CheckoutItem, newItem: CheckoutItem): Boolean {
            return oldItem == newItem
        }
    }

    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }
}