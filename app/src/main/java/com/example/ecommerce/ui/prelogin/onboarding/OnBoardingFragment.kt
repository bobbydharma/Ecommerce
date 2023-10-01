package com.example.ecommerce.ui.prelogin.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentOnBoardingBinding
import com.example.ecommerce.preference.PrefHelper
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreferencesManager.obCheck) {
            Log.d("onBoarding", "success")
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
        } else {
            Log.d("onBoarding", "Onboarding")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnboardingItems()

        binding.obSelanjutnya.setOnClickListener {
            binding.pager.currentItem += 1
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "OnBoarding_Next")
            }
        }

        binding.obLewati.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            sharedPreferencesManager.obCheck = true
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "OnBoarding_Skip")
            }
        }

        binding.obGabung.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_registerFragment)
            sharedPreferencesManager.obCheck = true
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "OnBoarding_Join")
            }
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

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == viewPagerAdapter.itemCount - 1) {
                    binding.obSelanjutnya.visibility = View.GONE
                } else {
                    binding.obSelanjutnya.visibility = View.VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.intoTabLayout, binding.pager) { _, _ -> }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

