package com.example.ecommerce.ui.prelogin.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentLoginBinding
import com.example.ecommerce.model.user.UserRequest
import com.example.ecommerce.preference.PrefHelper
import com.example.ecommerce.utils.Result
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.color.MaterialColors
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sharedPreferencesManager.obCheck == false ){
            findNavController().navigate(R.id.action_loginFragment_to_onBoardingFragment)
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

        var token = ""
        firebaseAnalytics = Firebase.analytics
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

        binding.btnMasuk.setOnClickListener {
            val data = UserRequest()
            data.email = binding.layoutEtEmailLogin.editText?.text.toString()
            data.password = binding.layoutEtPasswordLogin.editText?.text.toString()
            data.firebaseToken = token
            Log.d("FirebaseToken", token)
            viewModel.postLogin(data)
            firebaseAnalytics.logEvent("BUTTON_CLICK"){
                param("BUTTON_NAME", "Login" )
            }
        }
        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            firebaseAnalytics.logEvent("BUTTON_CLICK"){
                param("BUTTON_NAME", "Login_To_Register")
            }
        }

        spanText()
        validationButton()
        checking()



        viewModel.loginData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.prgressBarLogin.isVisible = false
                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN){
                        param(FirebaseAnalytics.Param.METHOD, "email")
                    }
                    if (result.data.data.userName.isNullOrEmpty()){
                        findNavController().navigate(R.id.main_to_profile)
                    }else{
                        findNavController().navigate(R.id.prelogin_to_main)
                    }
                }
                is Result.Error -> {

                    binding.etEmail.setText("")
                    binding.etPassword.setText("")
                    binding.layoutEtEmailLogin.isErrorEnabled = true
                    binding.prgressBarLogin.isVisible = false
                    binding.layoutEtEmailLogin.error = result.exception.message
                    binding.layoutEtEmailLogin.requestFocus()

                }
                is Result.Loading -> {

                    binding.prgressBarLogin.isVisible = true

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
        val textDaftarFirst = getString(R.string.spannable_text_daftar_first)
        val textDaftar = getString(R.string.spannable_text_daftar)
        val combinedText = "$textDaftarFirst$textDaftar"

        val spannableString = SpannableString(combinedText)

        val startIndex = combinedText.indexOf(getString(R.string.syarat_dan_ketentuan))
        val endIndex = startIndex + getString(R.string.syarat_dan_ketentuan).length

        val kebijakanStartIndex = combinedText.indexOf(getString(R.string.kebijakan_privasi))
        val kebijakanEndIndex = kebijakanStartIndex + getString(R.string.kebijakan_privasi).length

        spannableString.setSpan(ForegroundColorSpan(MaterialColors.getColor(requireView(), android.R.attr.colorPrimary)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(MaterialColors.getColor(requireView(), android.R.attr.colorPrimary)), kebijakanStartIndex, kebijakanEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        binding.syarat.text = spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}