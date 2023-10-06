package com.example.ecommerce.ui.main.payment


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.core.model.checkout.ItemPayment
import com.example.ecommerce.databinding.ItemPaymentBinding

class ChildPaymentAdapter constructor(
    private val itemOnClick: (ItemPayment) -> Unit
) : androidx.recyclerview.widget.ListAdapter<ItemPayment, ChildPaymentAdapter.ChildPaymentViewHolder>(
    itemPaymentDiffCallback
) {

    inner class ChildPaymentViewHolder(private val binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemPayment) {

            Glide.with(itemView.context)
                .load(item.image)
                .into(binding.ivItemPayment)

            binding.tvLabelPayment.text = item.label

            if (item.status == false) {
                binding.itemPayment.isEnabled = false
                binding.itemPayment.alpha = 0.5F
            }

            binding.itemPayment.setOnClickListener {
                itemOnClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChildPaymentAdapter.ChildPaymentViewHolder {
        val binding =
            ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChildPaymentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    object itemPaymentDiffCallback : DiffUtil.ItemCallback<ItemPayment>() {
        override fun areItemsTheSame(oldItem: ItemPayment, newItem: ItemPayment): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: ItemPayment, newItem: ItemPayment): Boolean {
            return oldItem == newItem
        }
    }

}