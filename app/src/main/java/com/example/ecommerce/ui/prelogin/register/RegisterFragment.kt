package com.example.ecommerce.ui.prelogin.register

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.model.UserRequest
import com.example.ecommerce.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class registerFragment : Fragment() {

    private val viewModel by viewModels<RegisterViewModel>()
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spanText()

        binding.btnDaftarDaftar.setOnClickListener {
            val data = UserRequest()
            data.email = binding.etEmail.toString()
            data.password =  binding.etPassword.toString()

            viewModel.registerData.observe(viewLifecycleOwner){ result ->
                when(result){
                    is Result.Success -> {
                        findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
                        Log.d("register", result.data.toString())
                    }
                    is Result.Error -> {
                        val error = result.exception
                        Log.d("register", error.toString())
                    }
                    is Result.Loading ->{

                    }
                }
            }
            viewModel.postRegister(data)
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
        binding.syaratDaftar.text = spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}