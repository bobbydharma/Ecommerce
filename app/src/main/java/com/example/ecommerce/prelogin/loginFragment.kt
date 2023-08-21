package com.example.ecommerce.prelogin

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentLoginBinding


class loginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMasuk.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }

        checking()
        spanText()
        validationButton()

        binding.btnDaftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    private fun validationButton() {
        binding.btnMasuk.isEnabled = false

        if (binding.layoutEtEmai.isErrorEnabled == false && binding.layoutEtEmai.isNotEmpty() && binding.layoutEtPassword.isErrorEnabled == false && binding.layoutEtPassword.isNotEmpty()){
            binding.btnMasuk.isEnabled = true
        }else{
            binding.btnMasuk.isEnabled = false
        }
    }

    private fun spanText() {
        val spannable = SpannableString("Dengan daftar disini, kamu menyetujui Syarat & Ketentuan serta Kebijakan Privasi TokoPhincon.")
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            37, // start
            57, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(Color.BLUE),
            63, // start
            79, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.syarat.text = spannable
    }

    private fun checking() {


        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length < 8 && p0!!.isNotEmpty()){
                    binding.layoutEtPassword.isErrorEnabled = true
                    binding.layoutEtPassword.error = "Password Tidak Valid"
                }else{
                    binding.layoutEtPassword.isErrorEnabled = false
                }
            }

        })

        binding.etEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (!Patterns.EMAIL_ADDRESS.matcher(p0).matches() && p0!!.isNotEmpty()){
                    binding.layoutEtEmai.isErrorEnabled = true
                    binding!!.layoutEtEmai.error = "Email tidak valid"
                }else{
                    binding.layoutEtEmai.isErrorEnabled = false
                }

            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}