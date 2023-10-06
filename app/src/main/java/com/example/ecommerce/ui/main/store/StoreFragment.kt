package com.example.ecommerce.ui.main.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.core.model.products.Items
import com.example.ecommerce.core.preference.PrefHelper
import com.example.ecommerce.databinding.FragmentStoreBinding
import com.example.ecommerce.ui.main.store.adapter.LoadStateAdapter
import com.example.ecommerce.ui.main.store.adapter.ProductsAdapter
import com.example.ecommerce.utils.formatToIDR
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class storeFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics


    @Inject
    lateinit var prefHelper: PrefHelper
    private val viewModel by activityViewModels<StoreViewModel>()
    private val pagingAdapter = ProductsAdapter(ProductsAdapter.ProductComparator) { itemClicked ->
        val bundle = bundleOf("id_product" to itemClicked.productId)
        val navController =
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
        navController.navigate(R.id.main_to_detail_product, bundle)

        //                start log event
        val itemProduct = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, itemClicked.productId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, itemClicked.productName)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, itemClicked.brand)
            putDouble(FirebaseAnalytics.Param.PRICE, (itemClicked.productPrice).toDouble())
        }
        val itemProductCart = Bundle(itemProduct).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }
//                end log event

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, itemClicked.productId)
            param(FirebaseAnalytics.Param.ITEM_NAME, itemClicked.productName)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemProductCart))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearchStore.setText(viewModel.prooductQuery.value.search)
        viewModel.prooductQuery.value.sort?.let { Log.d("sort", it) }
//        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), viewModel.spanCount)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "search",
            viewLifecycleOwner
        ) { requestKey, bundle ->
            val search = bundle.getString("searchItem")
            val newQuery = com.example.ecommerce.core.model.products.ProductsRequest()
            newQuery.search = search
            binding.etSearchStore.setText(search)
            setChipFiltered()
            viewModel.updateQuery(
                search = search,
                brand = viewModel.prooductQuery.value.brand,
                sort = viewModel.prooductQuery.value.sort,
                lowest = viewModel.prooductQuery.value.lowest,
                highest = viewModel.prooductQuery.value.highest,
                sortId = viewModel.prooductQuery.value.sortId
            )
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS) {
                search?.let { param(FirebaseAnalytics.Param.SEARCH_TERM, it) }
            }

            binding.rvProduct.layoutManager?.scrollToPosition(0)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "filter",viewLifecycleOwner
        ) { requestKey, bundle ->
            binding.rvProduct.layoutManager?.scrollToPosition(0)
        }

        binding.rvProduct.adapter = pagingAdapter.withLoadStateFooter(
            footer = LoadStateAdapter(pagingAdapter::retry)
        )
        binding.rvProduct.apply {
            itemAnimator?.changeDuration = 0
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.swipeRefresh.setOnRefreshListener {
            pagingAdapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.btnResetError.setOnClickListener {
            binding.errorData.isVisible = false
            binding.containerFilter.isVisible = true
            binding.rvProduct.isVisible = true
            binding.etSearchStore.setText("")
            viewModel.updateQuery(
                search = null,
                brand = null,
                sort = null,
                highest = null,
                lowest = null,
                sortId = null
            )
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Store_Reset")
            }
        }

        binding.btnRefreshConnection.setOnClickListener {
            pagingAdapter.refresh()
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Store_Refresh")
            }
        }

        binding.btnFilterStore.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetFragment.tag
            )
            firebaseAnalytics.logEvent("BUTTON_CLICK") {
                param("BUTTON_NAME", "Store_Filter")
            }
        }

        binding.etSearchStore.setOnClickListener {
            val searchFragment = SearchFragment()
            searchFragment.show(requireActivity().supportFragmentManager, "MyDialogFragment")
        }

        binding.btnToggle.setOnCheckedChangeListener { _, isChecked ->
            pagingAdapter.isGridMode = isChecked
            setLayoutManager(isChecked)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.querySearchResults.collectLatest {
                pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.chipFiltered.removeAllViews()
                setChipFiltered()
                logEventItemView(it)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->

                val gridLayoutManager = binding.rvProduct.layoutManager as GridLayoutManager
                val numberOfColumns = gridLayoutManager.spanCount
                if (loadStates.refresh is LoadState.Loading) {
                    if (numberOfColumns == 1) {
                        binding.shimmerList.isVisible = true
                        binding.rvProduct.isVisible = false
                        binding.errorConnection.isVisible = false
                        binding.containerFilter.isVisible = false
                    } else {
                        binding.shimmerGrid.isVisible = true
                        binding.rvProduct.isVisible = false
                        binding.errorConnection.isVisible = false
                        binding.containerFilter.isVisible = false
                    }
                }

                if (loadStates.refresh is LoadState.NotLoading) {
                    binding.shimmerList.isVisible = false
                    binding.shimmerGrid.isVisible = false
                    binding.errorConnection.isVisible = false
                    binding.containerFilter.isVisible = true
                    binding.rvProduct.isVisible = true
                }

                if (loadStates.refresh is LoadState.Error) {
                    when (val error = (loadStates.refresh as LoadState.Error).error) {
                        is HttpException -> {
                            if (error.code() == 401) {
                                binding.shimmerGrid.isVisible = true
                                binding.shimmerList.isVisible = false
                                binding.containerFilter.isVisible = false
                                binding.rvProduct.isVisible = false
                                binding.internalServerError.isVisible = false
                                binding.errorConnection.isVisible = true
                                Log.d("LoadState.Error", "HttpException 401")
                                pagingAdapter.retry()
                            } else if (error.code() == 404) {
                                binding.shimmerGrid.isVisible = false
                                binding.shimmerList.isVisible = false
                                binding.containerFilter.isVisible = false
                                binding.rvProduct.isVisible = false
                                binding.errorConnection.isVisible = false
                                binding.internalServerError.isVisible = false
                                binding.errorData.isVisible = true
                                Log.d("LoadState.Error", "HttpException 404")
                            } else {
                                binding.shimmerGrid.isVisible = false
                                binding.shimmerList.isVisible = false
                                binding.containerFilter.isVisible = false
                                binding.rvProduct.isVisible = false
                                binding.errorConnection.isVisible = false
                                binding.internalServerError.isVisible = true
                                binding.errorData.isVisible = false
                                Log.d("LoadState.Error", "HttpException else")
                            }
                        }

                        is IOException -> {
                            binding.shimmerGrid.isVisible = true
                            binding.shimmerList.isVisible = false
                            binding.containerFilter.isVisible = false
                            binding.rvProduct.isVisible = false
                            binding.errorConnection.isVisible = true
                            Log.d("LoadState.Error", "IOException")
                            pagingAdapter.retry()
                        }

                        else -> {
                            Log.d("LoadState.Error", "else")
                            val errorMessage = error.message
                            val httpStatusCode = errorMessage?.split(":")?.lastOrNull()?.trim()
                            if (httpStatusCode != null) {
                                when (httpStatusCode) {
                                    "404" -> {
                                        binding.shimmerGrid.isVisible = false
                                        binding.shimmerList.isVisible = false
                                        binding.containerFilter.isVisible = false
                                        binding.rvProduct.isVisible = false
                                        binding.errorConnection.isVisible = false
                                        binding.errorData.isVisible = true
                                        Log.d("LoadState.Error", "else 404")
                                    }

                                    "401" -> {
                                        binding.shimmerGrid.isVisible = true
                                        binding.shimmerList.isVisible = false
                                        binding.containerFilter.isVisible = false
                                        binding.rvProduct.isVisible = false
                                        binding.errorConnection.isVisible = true
                                        Log.d("LoadState.Error", "else 401")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun logEventItemView(it: PagingData<Items>) {

        val item = pagingAdapter.snapshot().map {
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, it?.productId)
                putString(FirebaseAnalytics.Param.ITEM_NAME, it?.productName)
                putString(FirebaseAnalytics.Param.ITEM_BRAND, it?.brand)
                it?.productPrice?.let { it1 ->
                    putDouble(
                        FirebaseAnalytics.Param.PRICE,
                        it1.toDouble()
                    )
                }
            }
        }

        val product = item.mapIndexed { index, bundle ->
            bundle.apply {
                putString(FirebaseAnalytics.Param.INDEX, index.toString())
            }
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, "Store")
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Store")
            param(FirebaseAnalytics.Param.ITEMS, product.toTypedArray())
        }
    }


    private fun setLayoutManager(gridMode: Boolean) {
        val firstVisibleItemPosition =
            (binding.rvProduct.layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0
        if (gridMode) {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvProduct.layoutManager = layoutManager

            val footerAdapter = LoadStateAdapter(pagingAdapter::retry)
            binding.rvProduct.adapter = pagingAdapter.withLoadStateFooter(footer = footerAdapter)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == pagingAdapter.itemCount && footerAdapter.itemCount > 0) {
                       2
                    } else {
                        1
                    }
                }
            }
            binding.rvProduct.layoutManager?.scrollToPosition(firstVisibleItemPosition)

        } else {
            binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 1)
            binding.rvProduct.layoutManager?.scrollToPosition(firstVisibleItemPosition)
        }
    }

    private fun setChipFiltered() {
        Log.d("Set Chip", "masuk")
        val map = mapOf(
            "1" to getString(R.string.ulasan),
            "2" to getString(R.string.penjualan),
            "3" to getString(R.string.harga_terendah),
            "4" to getString(R.string.harga_tertinggi)
        )

        binding.chipFiltered.removeAllViews()
        viewModel.prooductQuery.value.apply {
            map[viewModel.prooductQuery.value.sortId]?.let { setChip(it) }
            viewModel.prooductQuery.value.sortId?.let { Log.d("map", it) }
            brand?.let { setChip(brand!!) }
            lowest?.let { setChip("> ${lowest!!.formatToIDR()}") }
            highest?.let { setChip("< ${highest!!.formatToIDR()}") }
        }
    }

    private fun setChip(string: String) {
        val chip = Chip(requireContext())
        chip.text = string
        chip.isSelected = true
        chip.setTextAppearance(R.style.TextAppearance_App_BodyMedium)
        binding.chipFiltered.addView(chip)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}