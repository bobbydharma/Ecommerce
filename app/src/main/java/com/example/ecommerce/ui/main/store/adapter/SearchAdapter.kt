package com.example.ecommerce.ui.main.store.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemSearchBinding

class SearchAdapter(private val searchList: List<String>,private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(search: String) {
            binding.tvSearch.text = search
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList[position])

        holder.itemView.setOnClickListener {
            onItemClick(searchList[position])
        }

    }

    override fun getItemCount(): Int {
        return searchList.size
    }
}