package com.example.ecommerce.ui.prelogin.OnBoarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R

class ViewPagerAdapter(private val onBoardingItem: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>(){

    inner class ViewPagerViewHolder(view:View): RecyclerView.ViewHolder(view){

        private val ivOnboarding = view.findViewById<ImageView>(R.id.iv_ob)

        fun bind(onboardingItem: Int){
            ivOnboarding.setImageResource(onboardingItem)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerViewHolder {

        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_onboarding,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(onBoardingItem[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItem.size
    }

}