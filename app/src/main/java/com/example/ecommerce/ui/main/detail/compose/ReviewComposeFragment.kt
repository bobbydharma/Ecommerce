package com.example.ecommerce.ui.main.detail.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ecommerce.R
import com.example.ecommerce.core.model.products.ReviewProduct
import com.example.ecommerce.ui.main.detail.ReviewViewModel
import com.example.ecommerce.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewComposeFragment : Fragment() {

    private val viewModel by viewModels<ReviewViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ThemeCompose() {
                    ReviewScreen(viewModel,
                        onNavigateUp = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }

    @Composable
    fun ReviewScreen(
        viewModel: ReviewViewModel,
        onNavigateUp: () -> Unit
    ) {
        val reviewProduct by viewModel.reviewProduct.observeAsState()
        reviewProduct?.let {
            ReviewScreenItem(
                it,
                onNavigateUp = { onNavigateUp() },
                { viewModel.id?.let { it1 -> viewModel.getReviewProduct(it1) } }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ReviewScreenItem(
        reviewProduct: Result<ReviewProduct> = Result.Loading,
        onNavigateUp: () -> Unit,
        refresh: () -> Unit
    ) {
        val poppins_regular = FontFamily(Font(R.font.poppins_regular))
        val poppins_medium = FontFamily(Font(R.font.poppins_medium))
        val poppins_semi_bold = FontFamily(Font(R.font.poppins_semibold))
        val poppins_bold = FontFamily(Font(R.font.poppins_bold))
        when (reviewProduct) {
            is Result.Success -> {
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(id = R.string.ulasan_pembeli),
                                        fontFamily = poppins_regular,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
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
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {

                        LazyColumn {
                            items(reviewProduct.data.data.size) { index ->
                                ItemReviewScreen(reviewProduct.data.data[index])
                            }
                        }
                    }
                }
            }

            is Result.Loading -> {
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(id = R.string.ulasan_pembeli),
                                        fontFamily = poppins_regular,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
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
                    }
                ) { innerPadding ->
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
            }

            is Result.Error -> {
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = stringResource(id = R.string.ulasan_pembeli),
                                        fontFamily = poppins_regular,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
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
                    }
                ) { innerPadding ->
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
                            text = stringResource(id = R.string.connection),
                            fontFamily = poppins_medium,
                            fontSize = 32.sp
                        )
                        Text(
                            text = "Your connection is unavailable",
                            fontFamily = poppins_regular,
                            fontSize = 16.sp
                        )

                        Button(
                            onClick = {
                                refresh()
                            },
                        ) {
                            Text(stringResource(id = R.string.refresh))
                        }
                    }
                }
            }

            else -> {}
        }

    }
}
//    @Preview
//    @Composable
//    fun GreetingPreview() {
//        ReviewScreen()
//    }
//}

