package com.example.ecommerce.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentMainBinding
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.room.AppDatabase
import com.example.ecommerce.ui.main.cart.CartViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
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
    private val viewModel by viewModels<MainViewModel>()

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

        binding.bnvChild.setupWithNavController(navController)
        binding.topAppBar.setTitle(prefHelper.nama)
        val badgeDrawable = binding.bnvChild.getOrCreateBadge(R.id.wishlistFragment)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cart -> {
                    val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                    navController.navigate(R.id.main_to_cart)
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.itemWishlist.collectLatest {
                val count = it.count()
                if (count != 0){
                    badgeDrawable.isVisible = true
                    badgeDrawable.number = count
                }else{
                    badgeDrawable.isVisible = false
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