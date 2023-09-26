package com.example.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentMainBinding
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.room.AppDatabase
import com.example.ecommerce.ui.main.cart.CartViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val navHostFragment: NavHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.nhf_child) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }
    @Inject
    lateinit var prefHelper: PrefHelper
    @Inject
    lateinit var firebaseAnalytics:FirebaseAnalytics
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (prefHelper.token == null){
            findNavController().navigate(R.id.main_to_prelogin)
        }else{
            if (prefHelper.nama.isNullOrEmpty()){
                findNavController().navigate(R.id.main_to_profile)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bnvChild?.setupWithNavController(navController)
            bnvChild?.setOnItemReselectedListener {  }

            navigationView?.setupWithNavController(navController)

            navigationRailView?.setupWithNavController(navController)
            bnvChild?.setOnItemReselectedListener {  }
        }

        binding.topAppBar.setTitle(prefHelper.nama)
        val wishlist = binding.bnvChild ?: binding.navigationRailView
        val badgeDrawable = wishlist?.getOrCreateBadge(R.id.wishlistFragment)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cart -> {
                    val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                    navController.navigate(R.id.main_to_cart)
                    firebaseAnalytics.logEvent("BUTTON_CLICK"){
                        param("BUTTON_NAME", "MainFragment_To_Cart" )
                    }
                    true
                }

                R.id.notification -> {
                    val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                    navController.navigate(R.id.main_to_notification)
                    firebaseAnalytics.logEvent("BUTTON_CLICK"){
                        param("BUTTON_NAME", "MainFragment_To_Notification" )
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.itemWishlist.collectLatest {
                val count = it.count()
                if (count != 0){
                    badgeDrawable?.isVisible = true
                    badgeDrawable?.number = count
                }else{
                    badgeDrawable?.isVisible = false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.itemNotification.collectLatest {
                val count = it.count()

                if (count != 0){
                    val badgeDrawable = BadgeDrawable.create(requireContext())
                    badgeDrawable.number = count
                    badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
                    BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.topAppBar, R.id.notification)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItem.collectLatest {

                val count = it.count()
                if (count != 0){
                    val badgeDrawable = BadgeDrawable.create(requireContext())
                    badgeDrawable.number = count
                    badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
                    BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.topAppBar, R.id.cart)
                }
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}