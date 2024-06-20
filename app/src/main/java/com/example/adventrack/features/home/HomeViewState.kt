package com.example.adventrack.features.home

import android.location.Location
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.model.PlaceModel
import com.example.adventrack.domain.model.UserModel

data class HomeViewState (
    val location: Location? = null,
    val locationModel: LocationModel? = null,
    val user: UserModel? = null,
    val places: List<PlaceModel> = emptyList(),
    val nearbyPlaces : List<PlaceModel> = emptyList()
)