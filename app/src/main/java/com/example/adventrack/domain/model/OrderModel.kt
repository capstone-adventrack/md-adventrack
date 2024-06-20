package com.example.adventrack.domain.model


data class OrderModel (
    val id: Int,
    val name: String,
    val place: String,
    val price: Double,
    val date: String,
    val quantity: Int,
)