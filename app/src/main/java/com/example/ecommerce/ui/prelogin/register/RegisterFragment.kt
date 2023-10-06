package com.example.ecommerce.ui.prelogin.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.core.model.user.UserRequest
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.utils.Result
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.color.MaterialColors
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel>()

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var token = ""

        spanText()
        validationButton()
        checking()

        Firebase.messaging.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                token = task.result
                Log.d("TAG suskess token", token)
            },
        )

        binding.btnDaftarDaftar.setOnClickListener {
            val data = UserRequest()
            data.email = binding.layoutEtEmail.editText?.text.toString()
            data.password = binding.layoutEtPassword.editText?.text.toString()
            data.firebaseToken = token
            viewModel.postRegister(data)
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Register")
            }
        }

        binding.btnMasukDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Register_To_Login")
            }
        }

        viewModel.registerData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.btnDaftarDaftar.isVisible = true
                    binding.progressBarRegister.isVisible = false
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                        param(FirebaseAnalytics.Param.METHOD, "email")
                    }
                    findNavController().navigate(R.id.prelogin_to_main)
                }

                is Result.Error -> {
                    binding.etEmail.setText("")
                    binding.etPassword.setText("")
                    binding.progressBarRegister.isVisible = false
                    binding.layoutEtEmail.isErrorEnabled = true
                    binding.layoutEtEmail.error = result.exception.message
                    binding.layoutEtEmail.requestFocus()
                }

                is Result.Loading -> {
                    binding.btnDaftarDaftar.isVisible = false
                    binding.progressBarRegister.isVisible = true
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
        val textDaftarFirst = getString(R.string.spannable_text_daftar_first)
        val textDaftar = getString(R.string.spannable_text_daftar)
        val combinedText = "$textDaftarFirst$textDaftar"

        val spannableString = SpannableString(combinedText)

        val startIndex = combinedText.indexOf(getString(R.string.syarat_dan_ketentuan))
        val endIndex = startIndex + getString(R.string.syarat_dan_ketentuan).length

        val kebijakanStartIndex = combinedText.indexOf(getString(R.string.kebijakan_privasi))
        val kebijakanEndIndex = kebijakanStartIndex + getString(R.string.kebijakan_privasi).length

        spannableString.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(
                MaterialColors.getColor(
                    requireView(),
                    android.R.attr.colorPrimary
                )
            ), kebijakanStartIndex, kebijakanEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.syaratDaftar.text = spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}