package com.example.adventrack.domain.model


data class OrderModel (
    val id: String? = null,
    val name: String?= null,
    val place: String?= null,
    val price: String?= null,
    val timestamp: String?= null,
    val quantity: Int? = null,
)