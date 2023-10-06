package com.example.ecommerce.ui.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.databinding.FragmentUlasanPembeliBinding
import com.example.ecommerce.core.model.products.ReviewProduct
import com.example.ecommerce.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: FragmentUlasanPembeliBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ReviewViewModel>()
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUlasanPembeliBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getReviewProduct(viewModel.id.toString())

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.reviewProduct.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    setDisplay(result)
                    binding.progressBarReview.isVisible = false
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {
                    binding.progressBarReview.isVisible = true
                }

                else -> {}
            }
        }

    }

    private fun setDisplay(result: Result.Success<com.example.ecommerce.core.model.products.ReviewProduct>) {
        reviewAdapter = ReviewAdapter(result.data.data)
        binding.rvReview.adapter = reviewAdapter
    }

}