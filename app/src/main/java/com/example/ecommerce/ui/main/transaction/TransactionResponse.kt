package com.example.ecommerce.ui.main.transaction

data class TransactionResponse (
    val code: Int,
    val message: String,
    val data: List<Datum>
)

data class Datum (
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val payment: String,
    val total: Int,
    val items: List<Item>,
    val rating: Long,
    val review: String,
    val image: String,
    val name: String
)

data class Item (
    val productId: String,
    val variantName: String,
    val quantity: Long
)