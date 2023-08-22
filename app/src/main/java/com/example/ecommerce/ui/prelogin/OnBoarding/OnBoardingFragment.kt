package com.example.ecommerce.ui.prelogin.OnBoarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentOnBoardingBinding
import com.example.ecommerce.model.PrefHelper
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root

        if(sharedPreferencesManager.obCheck == true){
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnboardingItems()

        binding.obSelanjutnya.setOnClickListener {
            binding.pager.currentItem += 1
        }

        binding.obLewati.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            sharedPreferencesManager.obCheck = true
        }

        binding.obGabung.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_registerFragment)
            sharedPreferencesManager.obCheck = true
        }

    }

    private fun setOnboardingItems() {
        val imageObList = listOf(
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3
        )


        viewPagerAdapter = ViewPagerAdapter(imageObList)
        binding.pager.adapter = viewPagerAdapter

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                if (position == viewPagerAdapter.itemCount - 1) {
                    binding.obSelanjutnya.visibility = View.GONE
                } else {
                    binding.obSelanjutnya.visibility = View.VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.intoTabLayout, binding.pager)
        { tab, position ->
            if (position == 3){
                binding.obSelanjutnya.visibility = View.GONE
            }else{
                binding.obSelanjutnya.visibility = View.VISIBLE
            }
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

