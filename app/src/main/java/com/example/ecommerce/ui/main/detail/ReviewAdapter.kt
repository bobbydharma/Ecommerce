package com.example.ecommerce.ui.main.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemSearchBinding
import com.example.ecommerce.databinding.ListReviewBinding
import com.example.ecommerce.core.model.products.DataReview

class ReviewAdapter(private val dataReview: List<com.example.ecommerce.core.model.products.DataReview>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: ListReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dataReview: com.example.ecommerce.core.model.products.DataReview) {
            Glide.with(itemView.context)
                .load(dataReview.userImage)
                .placeholder(R.drawable.image_thumbnail_detail)
                .error(R.drawable.image_thumbnail_detail)
                .into(binding.ivUserImageReview)
            binding.tvUserNameReview.text = dataReview.userName
            binding.tvUserReviewReview.text = dataReview.userReview
            binding.ratingBar.rating = dataReview.userRating.toFloat()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            ListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(dataReview[position])
    }

    override fun getItemCount(): Int {
        return dataReview.size
    }
}

//class ReviewAdapter(private val context: Context, private val dataReview: List<DataReview>) :
//    BaseAdapter() {
//
//    override fun getCount(): Int {
//        return dataReview.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return dataReview[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View
//        val holder: ReviewViewHolder
//
//        if (convertView == null) {
//            val inflater = LayoutInflater.from(context)
//            view = inflater.inflate(R.layout.list_review, parent, false)
//            holder = ReviewViewHolder(view)
//            view.tag = holder
//        } else {
//            view = convertView
//            holder = view.tag as ReviewViewHolder
//        }
//
//        holder.bind(dataReview[position])
//
//        return view
//    }
//
//    private class ReviewViewHolder(private val view: View) {
//        fun bind(dataReview: DataReview) {
//            val tvUserNameReview: TextView = view.findViewById(R.id.tvUserNameReview)
//            val tvUserReviewReview: TextView = view.findViewById(R.id.tvUserReviewReview)
//
//            tvUserNameReview.text = dataReview.userName
//            tvUserReviewReview.text = dataReview.userReview
//        }
//    }
//}