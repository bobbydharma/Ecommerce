package com.example.ecommerce.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val navHostFragment: NavHostFragment by lazy {
        childFragmentManager.findFragmentById(R.id.nhf_child) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bnvChild.setupWithNavController(navController)
        binding.bnvChild.setOnItemReselectedListener {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}