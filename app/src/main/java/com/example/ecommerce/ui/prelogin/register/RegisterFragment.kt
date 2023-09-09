package com.example.ecommerce.ui.prelogin.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.utils.Result
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false

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
        validationButton()
        checking()

        binding.btnDaftarDaftar.setOnClickListener {
            val data = UserRequest()
            data.email = binding.layoutEtEmail.editText?.text.toString()
            data.password = binding.layoutEtPassword.editText?.text.toString()
            viewModel.postRegister(data)
        }

        binding.btnMasukDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        viewModel.registerData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is Result.Error -> {
                    binding.etEmail.setText("")
                    binding.etPassword.setText("")
                    binding.layoutEtEmail.isErrorEnabled = true
                    binding.layoutEtEmail.error = result.exception.message
                    binding.layoutEtEmail.requestFocus()
                }

                is Result.Loading -> {

                }
            }
        }
    }

    private fun validationButton() {
        binding.btnDaftarDaftar.isEnabled = validEmail &&
                validPassword &&
                !binding.layoutEtEmail.editText?.text.isNullOrEmpty() &&
                !binding.layoutEtPassword.editText?.text.isNullOrEmpty()
    }

    private fun checking() {
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if ((text?.length ?: 0) < 8 && !text.isNullOrEmpty()) {
                binding.layoutEtPassword.isErrorEnabled = true
                binding.layoutEtPassword.error = "Password Tidak Valid"
                validPassword = false
            } else {
                binding.layoutEtPassword.isErrorEnabled = false
                validPassword = true
            }
            validationButton()
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(text ?: "").matches() && !text.isNullOrEmpty()) {
                binding.layoutEtEmail.isErrorEnabled = true
                binding.layoutEtEmail.error = "Email tidak valid"
                validEmail = false
            } else {
                binding.layoutEtEmail.isErrorEnabled = false
                validEmail = true
            }
            validationButton()
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