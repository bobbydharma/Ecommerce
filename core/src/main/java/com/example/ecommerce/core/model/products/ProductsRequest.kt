package com.example.ecommerce.core.model.products

import androidx.annotation.Keep

@Keep
data class ProductsRequest(
    var search: String? = null,
    var brand: String? = null,
    var lowest: Int? = null,
    var highest: Int? = null,
    var sort: String? = null,
    var sortId : String? = null,
    val limit: Int? = null,
    val page: Int? = null
)