package com.example.ecommerce.ui.main.detail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ecommerce.R
import com.example.ecommerce.core.model.products.DataReview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemReviewScreen(
    data: DataReview
) {
    val poppins_regular = FontFamily(Font(R.font.poppins_regular))
    val poppins_medium = FontFamily(Font(R.font.poppins_medium))
    val poppins_semi_bold = FontFamily(Font(R.font.poppins_semibold))
    val poppins_bold = FontFamily(Font(R.font.poppins_bold))
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.userImage)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.image_thumbnail_detail),
                contentDescription = "test",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(
                    text = data.userName,
                    fontSize = 12.sp,
                    fontFamily = poppins_semi_bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                FlowRow(
                    Modifier
                        .wrapContentHeight(align = Alignment.Top)
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    repeat(5) { index ->
                        if (index >= data.userRating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "ratting",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "ratting",
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }

                    }
                }
            }
        }
        Text(
            text = data.userReview,
            fontFamily = poppins_regular,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Divider(Modifier.padding(top = 16.dp))
    }
}

//    @RequiresApi(Build.VERSION_CODES.Q)
//    @Preview
//    @Composable
//    fun GreetingPreview() {
//        ItemReviewScreen()
//    }