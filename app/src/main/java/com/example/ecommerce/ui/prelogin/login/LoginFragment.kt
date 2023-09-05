package com.example.ecommerce.ui.prelogin.login

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
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesManager: PrefHelper
    private val viewModel by viewModels<LoginViewModel>()

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferencesManager.token != null) {
            if (sharedPreferencesManager.nama == null){
                findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
            }else{
                findNavController().navigate(R.id.prelogin_to_main)
            }
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
            val data = UserRequest()
            data.email = binding.layoutEtEmailLogin.editText?.text.toString()
            data.password = binding.layoutEtPasswordLogin.editText?.text.toString()
            viewModel.postLogin(data)
        }
        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        spanText()
        validationButton()
        checking()

        viewModel.loginData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    findNavController().navigate(R.id.prelogin_to_main)
                }
                is Result.Error -> {

                    binding.etEmail.setText("")
                    binding.etPassword.setText("")
                    binding.layoutEtEmailLogin.isErrorEnabled = true
                    binding.layoutEtEmailLogin.error = result.exception.message
                    binding.layoutEtEmailLogin.requestFocus()

                }
                is Result.Loading -> {
                }

                else -> {}
            }
        }

    }

    private fun validationButton() {
        binding.btnMasuk.isEnabled = validEmail &&
                validPassword &&
                !binding.layoutEtEmailLogin.editText?.text.isNullOrEmpty() &&
                !binding.layoutEtPasswordLogin.editText?.text.isNullOrEmpty()
    }

    private fun checking() {
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if ((text?.length ?: 0) < 8 && !text.isNullOrEmpty()) {
                binding.layoutEtPasswordLogin.isErrorEnabled = true
                binding.layoutEtPasswordLogin.error = "Password Tidak Valid"
                validPassword = false
            } else {
                binding.layoutEtPasswordLogin.isErrorEnabled = false
                validPassword = true
            }
            validationButton()
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(text ?: "").matches() && !text.isNullOrEmpty()) {
                binding.layoutEtEmailLogin.isErrorEnabled = true
                binding.layoutEtEmailLogin.error = "Email tidak valid"
                validEmail = false
            } else {
                binding.layoutEtEmailLogin.isErrorEnabled = false
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