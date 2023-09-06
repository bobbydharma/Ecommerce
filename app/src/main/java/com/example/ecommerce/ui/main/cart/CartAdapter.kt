package com.example.ecommerce.ui.main.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.room.entity.CartEntity
import java.text.NumberFormat

class CartAdapter(
    diffCallback: DiffUtil.ItemCallback<CartEntity>,
    private val deleteItemClick: (CartEntity) -> Unit,
    private val addItemClick: (CartEntity)-> Unit,
    private val minItemClick: (CartEntity) -> Unit,
    private val selectedItem : (CartEntity) -> Unit
    ) : ListAdapter<CartEntity, CartAdapter.CartViewHolder>(CartEntityDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemListCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

    }

    inner class CartViewHolder(private val binding: ItemListCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartEntity: CartEntity) {

            Glide.with(itemView.context)
                .load(cartEntity.image)
                .placeholder(R.drawable.image_thumbnail)
                .error(R.drawable.image_thumbnail)
                .into(binding.ivCartList)
            binding.tvCartNameList.text = cartEntity.productName
            binding.tvCartPrice.text = (cartEntity.productPrice + cartEntity.variantPrice).formatToIDR()
            binding.tvCartVarianNameList.text = cartEntity.variantName
            if (cartEntity.stock < 10){
                binding.tvCartStokList.text = "Sisa ${cartEntity.stock}"
                binding.tvCartStokList.setTextColor(Color.RED)
            }else{
                binding.tvCartStokList.text = "Stok ${cartEntity.stock}"
                binding.tvCartStokList.setTextColor(Color.BLACK)
            }
            binding.tvOrderQuantity.text = cartEntity.quantity.toString()
            binding.checkboxItem.isChecked = cartEntity.isSelected

            binding.btnDelateItemCart.setOnClickListener {
                deleteItemClick(cartEntity)
            }

            binding.btnPlusCart.setOnClickListener {
                addItemClick(cartEntity)
            }

            binding.btnMinCart.setOnClickListener {
                minItemClick(cartEntity)
            }

            binding.checkboxItem.setOnClickListener {
                selectedItem(cartEntity)
            }
        }
    }

    object CartEntityDiffCallback : DiffUtil.ItemCallback<CartEntity>() {
        override fun areItemsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }
    }

    fun Int.formatToIDR(): String {
        val localeID = java.util.Locale("in", "ID")
        val currencyFormatter = NumberFormat.getCurrencyInstance(localeID)
        return currencyFormatter.format(this).replace(",00","")
    }

}