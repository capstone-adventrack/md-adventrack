package com.example.adventrack.data.remote.mapper

import com.example.adventrack.data.remote.response.LocationResponse
import com.example.adventrack.domain.model.ActivityTicketModel
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.model.PlaceModel
import com.google.firebase.firestore.DocumentSnapshot

fun LocationResponse.toFirstCity() = LocationModel(
    country = country,
    name= name,
    state = state
)

fun DocumentSnapshot.toPlaceModel()= PlaceModel(
    id = getString("id"),
    name = get("name") as String?,
    description = get("description") as String?,
    latitude = get("latitude") as Double?,
    longitude = get("longitude") as Double?,
    imageUrl = get("image_url") as List<String>,
    openTime = get("open_time") as String?,
    closeTime = get("close_time") as String?,
    rating = get("rating"),
    address = get("address") as String?,
    adultPrice = get("adult_price") as String?,
    childPrice = get("child_price") as String?,
    city = get("city") as String?,
    activityTicket = emptyList()
)