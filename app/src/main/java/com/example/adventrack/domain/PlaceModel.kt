package com.example.adventrack.domain

data class PlaceModel(
    val id : String,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val openTime: String,
    val closeTime: String,
    val rating: Float,
    val address: String,
    val adultPrice: String,
    val childPrice: String,
    val activityTicket: List<ActivityTicketModel>,
)
