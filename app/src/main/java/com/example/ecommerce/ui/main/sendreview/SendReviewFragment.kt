package com.example.ecommerce.ui.main.sendreview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentSendReviewBinding
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.formatToIDR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendReviewFragment : Fragment() {

    private var _binding : FragmentSendReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SendReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSendReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplay()
        var dataRating = 0
        binding.rattingbarSendReview.setOnRatingBarChangeListener { _, rating, _ ->
            dataRating = rating.toInt()
        }

        binding.btnDoneSendReview.setOnClickListener {
            if (viewModel.invoice != null){
                if (binding.layoutEtReviewSendReview.editText?.text.isNullOrEmpty()){
                    val ratingRequest = RatingRequest(viewModel.invoice!!.invoiceId, dataRating, "")
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.postRating(ratingRequest)
                    }
                }else{
                    val ratingRequest = RatingRequest(viewModel.invoice!!.invoiceId, dataRating, binding.layoutEtReviewSendReview.editText?.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.postRating(ratingRequest)
                    }
                }

            }
        }

        viewModel.ratingResponse.observe(viewLifecycleOwner){result ->
            when (result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.action_sendReviewFragment_to_main_navigation)
                }
                is Result.Error -> {
                    Log.d("error", result.toString())
                }
                is Result.Loading -> {
                }
                else -> {
                }
            }
        }

    }

    private fun setDisplay() {
        viewModel.invoice?.apply {
            binding.tvIdInvoiceSendReview.text = this?.invoiceId
            if (this?.status == true){
                binding.tvStatusSendReview.text = "Berhasil"
            }else{
                binding.tvStatusSendReview.text = "Gagal"
            }
            binding.tvDateSendReview.text = this?.date
            binding.tvTimeSendReview.text = this?.time
            binding.tvPaymentSendReview.text = this?.payment
            binding.tvTotalSendReview.text = this?.total?.formatToIDR()
        }

    }

}