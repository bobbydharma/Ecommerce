package com.example.ecommerce.ui.main.store

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.R
import com.example.ecommerce.core.model.products.ProductsRequest
import com.example.ecommerce.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<StoreViewModel>()
    var cekHighest: Boolean = false
    var cekLowest: Boolean = false
    var cekSort: Boolean = false
    var cekBrand: Boolean = false
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

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

        val map = mapOf(
            "1" to getString(R.string.ulasan),
            "2" to getString(R.string.penjualan),
            "3" to getString(R.string.harga_terendah),
            "4" to getString(R.string.harga_tertinggi)
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.prooductQuery.collectLatest {
                binding.btnResetFilter.isVisible =
                    !(it.sort.isNullOrEmpty() && it.brand.isNullOrEmpty() && it.highest == null && it.highest == null)

                map[it.sortId]?.let { it1 -> binding.chipGroupUrutkan.selectChipByText(it1) }
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
                            data.sortId = it.tag.toString()
                        }

                        R.id.chip_penjualan -> {
                            data.sort = it.text.toString()
                            data.sortId = it.tag.toString()
                        }

                        R.id.chip_harga_terendah -> {
                            data.sort = it.text.toString()
                            data.sortId = it.tag.toString()
                        }

                        R.id.chip_harga_tertinggi -> {
                            data.sort = it.text.toString()
                            data.sortId = it.tag.toString()
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

                        else -> {}
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
                highest = data.highest,
                sortId = data.sortId
            )
            requireActivity().supportFragmentManager.setFragmentResult(
                "filter",
                bundleOf("filter" to "data")
            )

//            logEventFilter(data)

            dismiss()
        }

        binding.btnResetFilter.setOnClickListener {
            binding.chipGroupUrutkan.resetSelected()
            binding.chipGroupBrand.resetSelected()
            binding.etLowest.text = null
            binding.etHighest.text = null
        }
    }

    private fun logEventFilter(data: ProductsRequest) {
        val itemfilter = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEMS, "${data.sort}, ${data.brand}, ${data.lowest}, ${data.highest}")
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_NAME, "FILTER")
            param(FirebaseAnalytics.Param.ITEMS, itemfilter)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}