package com.example.ecommerce.ui.main.store.adapter

import android.view.View
import com.example.ecommerce.databinding.ItemSearchBinding
import com.example.ecommerce.utils.BaseAdapter

//    inflaterFactory = { layoutInflater, parent, attachToParent ->
//        ItemSearchBinding.inflate(layoutInflater, parent, attachToParent)
//    }
class SearchAdapterGeneric(
    private val onItemClick: (String) -> Unit,
) : BaseAdapter<String, ItemSearchBinding>(ItemSearchBinding::inflate){
    override fun onItemBind(): (String, ItemSearchBinding, View) -> Unit {
        return { item, binding, view ->
            binding.tvSearch.text = item
            view.setOnClickListener {
                onItemClick(item)
            }
        }
    }


}