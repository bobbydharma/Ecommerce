package com.example.ecommerce.ui.main.payment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.databinding.ItemPaymentBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.ui.main.cart.CartAdapter

class ChildPaymentAdapter constructor(
    private val itemOnClick: (Item) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Item, ChildPaymentAdapter.ChildPaymentViewHolder>(itemPaymentDiffCallback) {

    inner class ChildPaymentViewHolder(private val binding: ItemPaymentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (item: Item){

            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPayment)

            binding.tvLabelPayment.text = item.label

            if (item.status == false){
                binding.itemPayment.isEnabled = false
                binding.itemPayment.alpha = 0.5F
            }

            binding.itemPayment.setOnClickListener {
                itemOnClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildPaymentAdapter.ChildPaymentViewHolder {
        val binding =
            ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildPaymentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    object itemPaymentDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

}