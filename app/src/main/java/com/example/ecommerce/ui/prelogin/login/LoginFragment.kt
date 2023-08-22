package com.example.ecommerce.ui.prelogin.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.preference.PrefHelper
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferencesManager.token != null) {
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }
        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        spanText()
        validationButton()
        checking()
    }

    private fun validationButton() {
        binding.btnMasuk.isEnabled = validEmail &&
                validPassword &&
                !binding.layoutEtEmai.editText?.text.isNullOrEmpty() &&
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
                binding.layoutEtEmai.isErrorEnabled = true
                binding.layoutEtEmai.error = "Email tidak valid"
                validEmail = false
            } else {
                binding.layoutEtEmai.isErrorEnabled = false
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
        binding.syarat.text = spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}