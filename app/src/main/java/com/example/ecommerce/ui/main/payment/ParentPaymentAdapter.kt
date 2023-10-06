package com.example.ecommerce.ui.main.payment


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.core.model.checkout.DataPayment
import com.example.ecommerce.core.model.checkout.ItemPayment
import com.example.ecommerce.databinding.SectionPaymentBinding

class ParentPaymentAdapter constructor(
    private val itemOnClick: (ItemPayment) -> Unit
) : androidx.recyclerview.widget.ListAdapter<DataPayment, ParentPaymentAdapter.ParentPaymentViewHolder>(
    itemPaymentDiffCallback
) {

    inner class ParentPaymentViewHolder(private val binding: SectionPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(datum: DataPayment) {

            binding.tvTitlePayment.text = datum.title
            val childPaymentAdapter = ChildPaymentAdapter({ item ->
                getItemParentPayment(item)
            })
            childPaymentAdapter.submitList(datum.item)
            binding.rvParentPayment.adapter = childPaymentAdapter
        }

        private fun getItemParentPayment(item: ItemPayment) {
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


    object itemPaymentDiffCallback : DiffUtil.ItemCallback<DataPayment>() {
        override fun areItemsTheSame(oldItem: DataPayment, newItem: DataPayment): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DataPayment, newItem: DataPayment): Boolean {
            return oldItem == newItem
        }
    }

}