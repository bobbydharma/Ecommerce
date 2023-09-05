package com.example.ecommerce.ui.main.store

import com.example.ecommerce.ui.main.store.adapter.SearchAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentSearchBinding
import com.example.ecommerce.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : DialogFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<StoreViewModel>()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearchFragment.setText(viewModel.prooductQuery.value.search)
        binding.etSearchFragment.requestFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchFragment, InputMethodManager.SHOW_IMPLICIT)
        binding.etSearchFragment.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.etSearchFragment.doOnTextChanged{text, actionId , _ , _ ->
            viewModel.setSearchTerm(text.toString())
        }

        binding.etSearchFragment.setOnEditorActionListener{_,actionId,event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)){
                val searchText = binding.etSearchFragment.text.toString()
                viewModel.search = searchText
                requireActivity().supportFragmentManager.setFragmentResult("search", bundleOf("searchItem" to searchText))
                dismiss()
                 true
            }else{
                false
            }

        }

        viewModel.searchData.observe(viewLifecycleOwner){result ->
            when (result) {
                is Result.Success -> {
                    setDisplayProducts(result.data.data)
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

                else -> {}
            }
        }

    }

    private fun setDisplayProducts(result: List<String>) {
        searchAdapter = SearchAdapter(result){ clickedItem->
            viewModel.search = clickedItem
            requireActivity().supportFragmentManager.setFragmentResult("search", bundleOf("searchItem" to clickedItem))
            dismiss()
        }
        binding.rvSearch.adapter = searchAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()

        // Menyembunyikan keyboard saat DialogFragment ditutup
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = dialog?.currentFocus
        focusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }
}