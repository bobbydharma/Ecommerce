package com.example.ecommerce.ui.main.store

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentBottomSheetBinding
import com.example.ecommerce.databinding.FragmentStoreBinding
import com.example.ecommerce.model.products.ProductsRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<StoreViewModel>()
    var cekHighest: Boolean = false
    var cekLowest: Boolean = false
    var cekSort: Boolean = false
    var cekBrand: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val themeWrapper = inflater.cloneInContext(
            ContextThemeWrapper(
                requireContext(),
                R.style.Base_Theme_Ecommerce
            )
        )
        _binding = FragmentBottomSheetBinding.inflate(
            inflater.cloneInContext(themeWrapper.context),
            container,
            false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateButtonReset()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.prooductQuery.collectLatest {
                updateButtonReset()
                binding.btnResetFilter.isVisible =
                    !(it.search.isNullOrEmpty() && it.sort.isNullOrEmpty() && it.brand.isNullOrEmpty() && it.highest == null && it.highest == null)

                it.sort?.let { it1 -> binding.chipGroupUrutkan.selectChipByText(it1) }
                it.brand?.let { it1 -> binding.chipGroupBrand.selectChipByText(it1) }
                if (it.lowest == null) {
                    binding.etLowest.setText("")
                } else {
                    binding.etLowest.setText(it.lowest.toString())
                }
                if (it.lowest == null) {
                    binding.etHighest.setText("")
                } else {
                    binding.etHighest.setText(it.highest.toString())
                }

            }
        }

        val behavior = BottomSheetBehavior.from(binding.root.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.apply {
            etHighest.doOnTextChanged { text, _, _, _ ->
                cekHighest = text != null
                if (text.isNullOrEmpty()) {
                    cekHighest = false
                }
                updateButtonReset()
            }

            etLowest.doOnTextChanged { text, _, _, _ ->
                cekLowest = text != null
                if (text.isNullOrEmpty()) {
                    cekLowest = false
                }
                updateButtonReset()
            }

            chipGroupUrutkan.setOnCheckedChangeListener { group, checkedIds ->
                cekSort = isAnyChipChecked(group)
                updateButtonReset()
            }

            chipGroupBrand.setOnCheckedChangeListener { group, checkedIds ->
                cekBrand = isAnyChipChecked(group)
                updateButtonReset()
            }
        }

        binding.btnShowFilter.setOnClickListener {
            val selectedChipIdSort = binding.chipGroupUrutkan.checkedChipId
            val selectedChipIdBrand = binding.chipGroupBrand.checkedChipId
            val data = ProductsRequest()

            if (selectedChipIdSort != View.NO_ID) {
                val selectedChipSort =
                    binding.chipGroupUrutkan.findViewById<Chip>(selectedChipIdSort)

                selectedChipSort?.let {
                    when (selectedChipSort.id) {
                        R.id.chip_ulasan -> {
                            data.sort = it.text.toString()
                        }

                        R.id.chip_penjualan -> {
                            data.sort = it.text.toString()
                        }

                        R.id.chip_harga_terendah -> {
                            data.sort = it.text.toString()
                        }

                        R.id.chip_harga_tertinggi -> {
                            data.sort = it.text.toString()
                        }
                    }
                }
            }

            if (selectedChipIdBrand != View.NO_ID) {
                val selectedChipBrand =
                    binding.chipGroupBrand.findViewById<Chip>(selectedChipIdBrand)

                selectedChipBrand?.let {
                    when (selectedChipBrand.id) {
                        R.id.chip_apple -> {
                            data.brand = it.text.toString()
                        }

                        R.id.chip_asus -> {
                            data.brand = it.text.toString()
                        }

                        R.id.chip_dell -> {
                            data.brand = it.text.toString()
                        }

                        R.id.chip_lenovo -> {
                            data.brand = it.text.toString()
                        }
                    }
                }
            }

            val lowest = binding.etLowest.text.toString()
            val highest = binding.etHighest.text.toString()

            if (lowest != null && lowest != "") {
                data.lowest = lowest.toInt()
            }

            if (highest != null && highest != "") {
                data.highest = highest.toInt()
            }

            viewModel.updateQuery(
                search = viewModel.prooductQuery.value.search,
                sort = data.sort,
                brand = data.brand,
                lowest = data.lowest,
                highest = data.highest
            )

            requireActivity().supportFragmentManager.setFragmentResult(
                "filter",
                bundleOf("filter" to "data")
            )
            dismiss()
        }

        binding.btnResetFilter.setOnClickListener {
            binding.chipGroupUrutkan.resetSelected()
            binding.chipGroupBrand.resetSelected()
            binding.etLowest.text = null
            binding.etHighest.text = null

//            viewModel.updateQuery(
//                search = null,
//                sort = null,
//                brand = null,
//                lowest = null,
//                highest = null)

        }
    }

    private fun updateButtonReset() {
        if (cekSort == false && cekBrand == false && cekHighest == false && cekLowest == false) {
            binding.btnResetFilter.isVisible = false
        } else {
            binding.btnResetFilter.isVisible = true
        }
    }

    fun isAnyChipChecked(chipGroup: ChipGroup): Boolean {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                return true
            }
        }
        return false
    }

    fun ChipGroup.resetSelected() {
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as Chip
            chip.isChecked = false
        }
    }

    fun ChipGroup.selectChipByText(chipText: String) {
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? Chip
            if (chip != null && chip.text == chipText) {
                chip.isChecked = true
                return
            }
        }
    }

}