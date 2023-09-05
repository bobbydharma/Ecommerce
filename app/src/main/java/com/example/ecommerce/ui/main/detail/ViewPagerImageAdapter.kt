package com.example.ecommerce.ui.prelogin.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ItemOnboardingBinding
import com.example.ecommerce.databinding.ItemOnboardingImageProductBinding

class ViewPagerImageAdapter(private val imageProductItem: List<String>) :
    RecyclerView.Adapter<ViewPagerImageAdapter.ViewPagerImageViewHolder>() {

    inner class ViewPagerImageViewHolder(private val binding: ItemOnboardingImageProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageProductItem: String) {
            Glide.with(itemView.context)
                .load(imageProductItem)
                .into(binding.ivDetailProduct)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerImageViewHolder {
        val binding =
            ItemOnboardingImageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        holder.bind(imageProductItem[position])
    }

    override fun getItemCount(): Int {
        return imageProductItem.size
    }
}