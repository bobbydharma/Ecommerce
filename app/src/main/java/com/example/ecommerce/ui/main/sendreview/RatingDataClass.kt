package com.example.ecommerce.ui.main.sendreview

data class RatingRequest (
    val invoiceId : String,
    val rating : Int,
    val review : String
)

data class RatingResponse(
    val code : Int,
    val message : String
)