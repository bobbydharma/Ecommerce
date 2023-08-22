package com.example.ecommerce.ui.prelogin.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.utils.Result
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spanText()

        binding.btnDaftarDaftar.setOnClickListener {
            val data = UserRequest()
            data.email = binding.etEmail.editText?.text.toString()
            data.password = binding.etPassword.editText?.text.toString()
            viewModel.postRegister(data)
        }

        viewModel.registerData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
                }

                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        result.exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {

                }
            }
        }
    }


    private fun spanText() {
        val spannable =
            SpannableString("Dengan daftar disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon.")
        spannable.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ),
            37, // start
            57, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ),
            63, // start
            79, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.syaratDaftar.text = spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}