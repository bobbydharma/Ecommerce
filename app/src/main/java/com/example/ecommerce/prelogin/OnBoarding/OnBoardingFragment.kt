package com.example.ecommerce.prelogin.OnBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentOnBoardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingFragment : Fragment() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root
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
        }

        binding.obGabung.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_registerFragment)
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

        TabLayoutMediator(binding.intoTabLayout, binding.pager)
        { tab, position ->
            if (position == binding.pager.size){
                binding.obSelanjutnya.isVisible = false
            }else{
                binding.obSelanjutnya.isVisible = true
            }
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}