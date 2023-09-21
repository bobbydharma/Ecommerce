package com.example.ecommerce.ui.main.notifcation

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemListCartBinding
import com.example.ecommerce.databinding.ItemNotificationBinding
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.NotificationEntity
import com.google.android.material.color.MaterialColors

class NotificationAdapter(
    diffCallback: DiffUtil.ItemCallback<NotificationEntity>,
    private val itemClick : (NotificationEntity) -> Unit
) : ListAdapter<NotificationEntity, NotificationAdapter.notificationViewHolder> (NotificaitionEntityDiffCallback) {

    inner class notificationViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationEntity: NotificationEntity) {

            Glide.with(itemView.context)
                .load(notificationEntity.image)
                .placeholder(R.drawable.image_thumbnail)
                .error(R.drawable.image_thumbnail)
                .into(binding.ivNotification)

            binding.tvBodyNotification.text = notificationEntity.body
            binding.tvTitleNotification.text = notificationEntity.title
            binding.dateTimeNotification.text = "${notificationEntity.date} ${notificationEntity.time}"

            if (!notificationEntity.isRead){
                binding.containerNotification.setBackgroundColor(MaterialColors.getColor(itemView, com.google.android.material.R.attr.colorPrimaryContainer))
            }else{
                binding.containerNotification.setBackgroundColor(MaterialColors.getColor(itemView, com.google.android.material.R.attr.colorSurfaceContainerLowest))
            }

            binding.containerNotification.setOnClickListener {
                itemClick(notificationEntity)
               }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return notificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: notificationViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    object NotificaitionEntityDiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem.idNotification == newItem.idNotification
        }

        override fun areContentsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
            return oldItem == newItem
        }
    }

}