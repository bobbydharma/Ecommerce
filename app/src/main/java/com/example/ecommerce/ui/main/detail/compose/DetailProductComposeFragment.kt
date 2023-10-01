package com.example.ecommerce.ui.main.detail.compose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ecommerce.R
import com.example.ecommerce.model.products.DataProductDetail
import com.example.ecommerce.model.products.ProductDetailResponse
import com.example.ecommerce.model.products.ProductVariant
import com.example.ecommerce.model.products.convertToCheckoutList
import com.example.ecommerce.model.products.mappingCart
import com.example.ecommerce.room.entity.CartEntity
import com.example.ecommerce.room.entity.WishlistEntity
import com.example.ecommerce.ui.main.detail.DetailProductViewModel
import com.example.ecommerce.utils.Result
import com.example.ecommerce.utils.formatToIDR
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class DetailProductComposeFragment : Fragment() {

    private val viewModel: DetailProductViewModel by viewModels()

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ThemeCompose() {
                    DetailProductScreen(
                        viewModel,
                        onNavigateUp = { findNavController().navigateUp() },
                        { dataProductDetail, index -> buyNow(dataProductDetail, index) },
                        { dataProductDetail, index -> addToCart(dataProductDetail, index) },
                        { idProduct -> onReviewClick(idProduct) },
                        shareLink = { shareLink() },
                        { cartEntity, bundle -> sendLogEventAddToCart(cartEntity, bundle) },
                        { cartEntity, bundle -> sendLogEventViewItem(cartEntity, bundle) },
                        { dataProductDetail, bundle ->
                            logEventAddToWishlist(
                                dataProductDetail,
                                bundle
                            )
                        }
                    )
                }
            }
        }
    }

    private fun logEventAddToWishlist(dataProductDetail: DataProductDetail, bundle: Bundle) {

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(
                FirebaseAnalytics.Param.VALUE,
                (dataProductDetail.productPrice + dataProductDetail.productVariant[0].variantPrice).toDouble()
            )
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundle))
        }

    }

    private fun sendLogEventAddToCart(cartEntity: CartEntity, bundle: Bundle) {
        //                start log event
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(
                FirebaseAnalytics.Param.VALUE,
                (cartEntity.productPrice + cartEntity.variantPrice).toDouble()
            )
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundle))
        }
//                 end log event
    }

    private fun sendLogEventViewItem(cartEntity: CartEntity, bundle: Bundle) {
        //                start log event
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.CURRENCY, "IDR")
            param(
                FirebaseAnalytics.Param.VALUE,
                (cartEntity.productPrice + cartEntity.variantPrice).toDouble()
            )
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(bundle))
        }
//                 end log event
    }

    private fun shareLink() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "http://com.example.ecommerce.ui.main.detail/${viewModel.id}"
        )
        val title = "Bagikan ke"
        val chooser = Intent.createChooser(intent, title)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        } else {
        }
    }

    private fun onReviewClick(idProduct: String) {
        val bundle = bundleOf("id_product_review" to idProduct)
        findNavController().navigate(
            R.id.action_detailProductFragment3_to_ulasanPembeliFragment2,
            bundle
        )
    }

    private fun addToCart(dataProductDetail: DataProductDetail, index: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val cartItem = viewModel.cekItem(dataProductDetail.productId)
            if (cartItem != null) {
                if (cartItem.stock > cartItem.quantity) {
                    viewModel.insertOrUpdateItem(dataProductDetail, index)
                } else {
                }
            } else {
                viewModel.insertOrUpdateItem(dataProductDetail, index)
            }
        }
    }

    private fun buyNow(dataProductDetail: DataProductDetail, index: Int) {
        val bundle = bundleOf("CheckoutList" to dataProductDetail.convertToCheckoutList(index))
        findNavController().navigate(R.id.action_detailProductFragment3_to_checkoutFragment, bundle)
    }

}

@Composable
fun DetailProductScreen(
    viewModel: DetailProductViewModel,
    onNavigateUp: () -> Unit,
    onBuyNow: (DataProductDetail, Int) -> Unit,
    addToCart: (DataProductDetail, Int) -> Unit,
    onReviewClick: (String) -> Unit,
    shareLink: () -> Unit,
    sendLogEventAddToCart: (CartEntity, Bundle) -> Unit,
    sendLogEventViewItem: (CartEntity, Bundle) -> Unit,
    logEventAddToWishlist: (DataProductDetail, Bundle) -> Unit
) {
    val detailProduct by viewModel.detailProduct.observeAsState()
    val itemWishList by viewModel.wishlistItem.collectAsState()
    val itemCart by viewModel.cartEntityFlow.collectAsState()
    detailProduct?.let {
        DetailProductScreen(
            it,
            onNavigateUp = { onNavigateUp() },
            { dataProductDetail, index -> onBuyNow(dataProductDetail, index) },
            { dataProductDetail, index -> addToCart(dataProductDetail, index) },
            { onReviewClick -> onReviewClick(onReviewClick) },
            { dataProductDetail, index -> viewModel.insertToWishlist(dataProductDetail, index) },
            { dataProductDetail -> viewModel.deleteWishlist(dataProductDetail) },
            itemWishList,
            shareLink = { shareLink() },
            itemCart,
            { viewModel.getDetailProduct(viewModel.id) },
            { cartEntity, bundle -> sendLogEventAddToCart(cartEntity, bundle) },
            { cartEntity, bundle -> sendLogEventViewItem(cartEntity, bundle) },
            { dataProductDetail, bundle -> logEventAddToWishlist(dataProductDetail, bundle) }
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun DetailProductScreen(
    detailProduct: Result<ProductDetailResponse> = Result.Loading,
    onNavigateUp: () -> Unit,
    onBuyNow: (DataProductDetail, Int) -> Unit,
    addToCart: (DataProductDetail, Int) -> Unit,
    onReviewClick: (String) -> Unit,
    addToWishList: (DataProductDetail, Int) -> Unit,
    deleteToWishList: (DataProductDetail) -> Unit,
    itemWishList: WishlistEntity?,
    shareLink: () -> Unit,
    itemCart: CartEntity?,
    refresh: () -> Unit,
    sendLogEventAddToCart: (CartEntity, Bundle) -> Unit,
    sendLogEventViewItem: (CartEntity, Bundle) -> Unit,
    logEventAddToWishlist: (DataProductDetail, Bundle) -> Unit
) {
    val poppins_regular = FontFamily(Font(R.font.poppins_regular))
    val poppins_medium = FontFamily(Font(R.font.poppins_medium))
    val poppins_semi_bold = FontFamily(Font(R.font.poppins_semibold))
    val poppins_bold = FontFamily(Font(R.font.poppins_bold))
    var globalIndex = rememberSaveable { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                androidx.compose.material3.Snackbar(
                    snackbarData = it,
                    containerColor = if (it.visuals.message.contains(
                            "Stok",
                            true
                        )
                    ) Color.Red else SnackbarDefaults.color
                )
            }
        },
        modifier = Modifier
            .background(Color.White),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.detail_product),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = poppins_regular,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onNavigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
                Divider()
            }
        },

        bottomBar = {
            if (detailProduct is Result.Success) {
                val itemDetailProduct = detailProduct.data.data
                //                start log event
                val itemProduct = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, detailProduct.data.data.productId)
                    putString(
                        FirebaseAnalytics.Param.ITEM_NAME,
                        detailProduct.data.data.productName
                    )
                    putString(
                        FirebaseAnalytics.Param.ITEM_VARIANT,
                        detailProduct.data.data.productVariant[globalIndex.value].variantName
                    )
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, detailProduct.data.data.brand)
                    putDouble(
                        FirebaseAnalytics.Param.PRICE,
                        (itemDetailProduct.productPrice + itemDetailProduct.productVariant[globalIndex.value].variantPrice).toDouble()
                    )
                }
                val itemProductCart = Bundle(itemProduct).apply {
                    putLong(FirebaseAnalytics.Param.QUANTITY, 1)
                }
//                end log event

                Column {
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onBuyNow(detailProduct.data.data, globalIndex.value)
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.beli_langsung),
                                fontFamily = poppins_regular
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                if (itemCart != null) {
                                    if (itemCart.stock > itemCart.quantity) {
                                        addToCart(detailProduct.data.data, globalIndex.value)
                                        Log.d(
                                            "itemCart.stock > itemCart.quantity",
                                            "${globalIndex.value}"
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Item ditambahkan")
                                        }
                                        sendLogEventAddToCart(itemCart, itemProductCart)
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Stok Habis")
                                        }
                                    }
                                } else {
                                    addToCart(detailProduct.data.data, globalIndex.value)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Item ditambahkan")
                                    }
                                    sendLogEventAddToCart(
                                        detailProduct.data.data.mappingCart(
                                            globalIndex.value
                                        ), itemProductCart
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.btn_tambah_keranjang),
                                fontFamily = poppins_regular
                            )
                        }
                    }
                }
            }
        }


    ) { innerPadding ->

        when (detailProduct) {
            is Result.Success -> {
                Log.d("detail product", "success")
                val itemDetailProduct = detailProduct.data.data
                val isWishlist = rememberUpdatedState(itemWishList != null)
                var quantitiyGlobal = remember { mutableStateOf(0) }
                if (itemCart != null) {
                    quantitiyGlobal.value = itemCart.quantity
                } else {
                    quantitiyGlobal.value = 0
                }

                //                start log event
                val itemProduct = Bundle().apply {
                    putString(FirebaseAnalytics.Param.ITEM_ID, detailProduct.data.data.productId)
                    putString(
                        FirebaseAnalytics.Param.ITEM_NAME,
                        detailProduct.data.data.productName
                    )
                    putString(
                        FirebaseAnalytics.Param.ITEM_VARIANT,
                        detailProduct.data.data.productVariant[globalIndex.value].variantName
                    )
                    putString(FirebaseAnalytics.Param.ITEM_BRAND, detailProduct.data.data.brand)
                    putDouble(
                        FirebaseAnalytics.Param.PRICE,
                        (itemDetailProduct.productPrice + itemDetailProduct.productVariant[globalIndex.value].variantPrice).toDouble()
                    )
                }
                val itemProductCart = Bundle(itemProduct).apply {
                    putLong(FirebaseAnalytics.Param.QUANTITY, 1)
                }
//                end log event

                sendLogEventViewItem(itemDetailProduct.mappingCart(0), itemProduct)

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box {
                        val pageCount = detailProduct.data.data.image.size
                        val pagerState = rememberPagerState()
                        HorizontalPager(
                            pageCount = pageCount,
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) { page ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(detailProduct.data.data.image[page])
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.image_thumbnail_detail),
                                contentDescription = "test",
                                modifier = Modifier
                                    .height(300.dp)
                            )

                        }
                        Column(
                            Modifier.align(Alignment.BottomCenter)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(pageCount) { iteration ->
                                    val color =
                                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .size(8.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    Divider()
                    Row(
                        Modifier
                            .padding(start = 16.dp, end = 14.dp, top = 10.dp)
                    ) {
                        Text(
                            text = (detailProduct.data.data.productPrice + detailProduct.data.data.productVariant[globalIndex.value].variantPrice).formatToIDR(),
                            fontSize = 20.sp,
                            maxLines = 2,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = poppins_regular,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                        )
                        IconButton(
                            onClick = {
                                shareLink()
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share link"
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            onClick = {
                                if (isWishlist.value) {
                                    deleteToWishList(detailProduct.data.data)
                                } else {
                                    addToWishList(detailProduct.data.data, globalIndex.value)
                                    logEventAddToWishlist(detailProduct.data.data, itemProductCart)
                                }

                            }) {
                            Icon(
                                imageVector = if (isWishlist.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist"
                            )
                        }
                    }
                    Text(
                        text = detailProduct.data.data.productName,
                        fontFamily = poppins_regular,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp
                    )
                    Row {
                        Text(
                            text = stringResource(R.string.terjual, detailProduct.data.data.sale),
                            fontFamily = poppins_regular,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .padding(top = 8.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Star Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 4.dp, end = 4.dp)
                            )
                            Text(
                                text = "${detailProduct.data.data.productRating} (${detailProduct.data.data.totalRating})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 8.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.pilih_varian),
                        fontSize = 16.sp,
                        fontFamily = poppins_medium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    FlowRow(
                        Modifier
                            .fillMaxWidth(1f)
                            .wrapContentHeight(align = Alignment.Top)
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        detailProduct.data.data.productVariant.forEachIndexed { index, productVariant ->
                            InputChip(
                                modifier = Modifier.padding(4.dp),
                                label = { Text(productVariant.variantName) },
                                selected = index == globalIndex.value,
                                onClick = {
                                    if (index == globalIndex.value) {
                                        globalIndex.value = index
                                    } else {
                                        globalIndex.value = index
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.deskripsi_produk),
                        fontSize = 16.sp,
                        fontFamily = poppins_medium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = detailProduct.data.data.description,
                        fontSize = 14.sp,
                        fontFamily = poppins_regular,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.ulasan_pembeli),
                            fontSize = 16.sp,
                            fontFamily = poppins_medium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        )
                        TextButton(onClick = {
                            onReviewClick(detailProduct.data.data.productId)
                        }) {
                            Text(
                                text = stringResource(R.string.lihat_semua),
                                fontFamily = poppins_regular,
                            )
                        }
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "ratting",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = detailProduct.data.data.productRating.toString(),
                            fontFamily = poppins_semi_bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .padding(start = 4.dp)
                        )
                        Text(
                            text = "/5.0",
                            fontFamily = poppins_regular,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .padding(bottom = 6.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 32.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.pembeli_merasa_puas,
                                    detailProduct.data.data.totalSatisfaction
                                ),
                                fontFamily = poppins_semi_bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = stringResource(
                                    R.string.rating_ulasan,
                                    detailProduct.data.data.totalRating,
                                    detailProduct.data.data.totalReview
                                ),
                                fontFamily = poppins_regular,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }


            is Result.Loading -> {
                Log.d("detail product", "Loading")

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 4.dp
                    )
                }
            }

            is Result.Error -> {
                Log.d("detail product", "Error")

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.error_image)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.error_image),
                        contentDescription = "test",
                        modifier = Modifier
                            .height(128.dp)
                    )
                    Text(
                        text = stringResource(R.string.connection),
                        fontFamily = poppins_medium,
                        fontSize = 32.sp
                    )
                    Text(
                        text = stringResource(R.string.your_connection_is_unavailable),
                        fontFamily = poppins_regular,
                        fontSize = 16.sp
                    )

                    Button(
                        onClick = {
                            refresh()
                        },
                    ) {
                        Text(stringResource(R.string.refresh))
                    }
                }
            }

            else -> {
                Log.d("detail product", "Else")
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(R.drawable.error_image)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.error_image),
                        contentDescription = "test",
                        modifier = Modifier
                            .height(128.dp)
                    )
                    Text(
                        text = stringResource(R.string.connection),
                        fontFamily = poppins_medium,
                        fontSize = 32.sp
                    )
                    Text(
                        text = stringResource(R.string.your_connection_is_unavailable),
                        fontFamily = poppins_regular,
                        fontSize = 16.sp
                    )

                    Button(
                        onClick = {
                            refresh()
                        },
                    ) {
                        Text(stringResource(R.string.refresh))
                    }
                }
            }

        }


    }


}

@Preview
@Composable
fun GreetingPreview() {
    val dummyProductVariant1 = ProductVariant("RAM 16GB", 100)
    val dummyProductVariant2 = ProductVariant("RAM 32GB", 120)

    val dummyDataProductDetail = DataProductDetail(
        productId = "Rp23.499.000",
        productName = "Lenovo Legion 7 16 I7 11800 16GB 1TB SSD RTX3070 8GB Windows 11 QHD IPS",
        productPrice = 23499000,
        image = listOf("image_url_1", "image_url_2", "image_url_3"),
        brand = "Lenovo",
        description = "Product Description",
        store = "Store Name",
        sale = 20,
        stock = 100,
        totalRating = 4,
        totalReview = 50,
        totalSatisfaction = 85,
        productRating = 4.5f,
        productVariant = listOf(dummyProductVariant1, dummyProductVariant2)
    )

    val dummyProductDetailResponse = ProductDetailResponse(
        code = 200,
        message = "Product details retrieved successfully",
        data = dummyDataProductDetail
    )
    DetailProductScreen(
        detailProduct = Result.Success(dummyProductDetailResponse),
        onNavigateUp = {},
        onBuyNow = { dataProductDetail, index -> },
        addToCart = { dataProductDetail, index -> },
        onReviewClick = { idProduct -> },
        addToWishList = { dataProductDetail, Int -> },
        deleteToWishList = { dataProductDetail -> },
        itemWishList = null,
        shareLink = { /*...*/ },
        itemCart = null,
        refresh = {},
        sendLogEventAddToCart = { cartEntity, bundle -> },
        sendLogEventViewItem = { cartEntity, bundle -> },
        logEventAddToWishlist = { dataProductDetail, bundle -> }
    )
}
