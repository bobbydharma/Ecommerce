package com.example.ecommerce.ui.main.payment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemPaymentBinding
import com.example.ecommerce.databinding.SectionPaymentBinding

class ParentPaymentAdapter constructor(
    private val itemOnClick: (Item) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Datum, ParentPaymentAdapter.ParentPaymentViewHolder>(
    itemPaymentDiffCallback
) {

    inner class ParentPaymentViewHolder(private val binding: SectionPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(datum: Datum) {

            binding.tvTitlePayment.text = datum.title
            val childPaymentAdapter = ChildPaymentAdapter({ item ->
                getItemParentPayment(item)
            })
            childPaymentAdapter.submitList(datum.item)
            binding.rvParentPayment.adapter = childPaymentAdapter
        }

        private fun getItemParentPayment(item: Item) {
            itemOnClick(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentPaymentAdapter.ParentPaymentViewHolder {
        val binding =
            SectionPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentPaymentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    object itemPaymentDiffCallback : DiffUtil.ItemCallback<Datum>() {
        override fun areItemsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem == newItem
        }
    }

}