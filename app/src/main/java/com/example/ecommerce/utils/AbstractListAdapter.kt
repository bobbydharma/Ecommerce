package com.example.ecommerce.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<Model : Any, ViewHolder : RecyclerView.ViewHolder>
    (
    private val onCreateViewHolder: (parent: ViewGroup, viewType: Int) -> ViewHolder,
    private val onBindViewHolder: (viewHolder: ViewHolder, position: Int, item: Model) -> Unit,
    private val differCallback: DiffUtil.ItemCallback<Model>,
    private val onViewType: ((viewType: Int, item: List<Model>) -> Int)? = null,
    private val onDetachFromWindow: ((ViewHolder) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {
    var item = listOf<Model>()
    private var onGetItemViewType: ((position: Int) -> Int)? = null
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        onCreateViewHolder.invoke(parent, viewType)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        onBindViewHolder.invoke(holder, position, item)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (onViewType != null) {
            onViewType.invoke(position, item)
        } else {
            val onGetItemViewType = onGetItemViewType
            onGetItemViewType?.invoke(position) ?: super.getItemViewType(position)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        onDetachFromWindow?.invoke(holder)
    }
}