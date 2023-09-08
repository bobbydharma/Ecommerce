package com.example.ecommerce.ui.main.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemTransactionBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.utils.formatToIDR

class TransactionAdapter (
    private val reviewClick: (Datum) -> Unit
) : ListAdapter<Datum, TransactionAdapter.TransactionViewHolder > (
    (DatumDiffCallback)
) {

    inner class TransactionViewHolder(private val binding : ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind ( item : Datum){

            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.image_thumbnail_detail)
                .error(R.drawable.image_thumbnail_detail)
                .into(binding.ivTrasaction)

            binding.tvDateTransaction.text = item.date
            binding.tvNameProductTransaction.text = item.name
            binding.tvTotalPriceTransaction.text = item.total.formatToIDR()
            binding.tvQuantityTransaction.text = "${item.items.size} barang"

            if (item.review.isNullOrEmpty()){
                binding.btnReviewTransaction.isVisible = true
            }else{
                binding.btnReviewTransaction.isVisible = false
            }

            binding.btnReviewTransaction.setOnClickListener {
                reviewClick(item)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    object DatumDiffCallback : DiffUtil.ItemCallback<Datum>() {
        override fun areItemsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem.invoiceId == newItem.invoiceId
        }

        override fun areContentsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem == newItem
        }
    }

}