package com.example.adventrack.domain.model


data class OrderModel (
    val id: String?,
    val name: String,
    val place: String,
    val price: String,
    val date: String,
    val quantity: Int,
)