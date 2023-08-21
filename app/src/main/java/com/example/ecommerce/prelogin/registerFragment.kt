package com.example.ecommerce.prelogin

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.di.AppModule
import com.example.ecommerce.model.RegisterResponse
import com.example.ecommerce.model.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class registerFragment : Fragment() {

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
            viewLifecycleOwner.lifecycleScope.launch {
                postRegister(RegisterRequest(binding.etEmail.toString(), binding.etPassword.toString()))
            }
        }

    }


    private fun postRegister(data : RegisterRequest) {
        AppModule.instance.postRegister(
            "6f8856ed-9189-488f-9011-0ff4b6c08edc",
            data
        ).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                findNavController().navigate(R.id.profileFragment)
                binding.tvTest.text =  response.code().toString()

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                binding.tvTest.text =  t.message
            }

        })
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