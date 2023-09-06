package com.example.ecommerce.ui.main.sendreview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentSendReviewBinding


class SendReviewFragment : Fragment() {

    private var _binding : FragmentSendReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

}