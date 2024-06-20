package com.example.adventrack.domain.model

data class PlaceModel(
    val id : String? = null,
    val name: String? = null,
    val description: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val imageUrl: List<String> = emptyList(),
    val openTime: String? = null,
    val closeTime: String? = null,
    val rating: Any? = null,
    val address: String? = null,
    val adultPrice: String? = null,
    val childPrice: String? = null,
    val city: String? = null,
    val activityTicket: List<ActivityTicketModel> = emptyList(),
)
