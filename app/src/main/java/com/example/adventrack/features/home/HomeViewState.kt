package com.example.adventrack.features.home

import android.location.Location
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.model.PlaceModel

data class HomeViewState (
    val location: Location? = null,
    val locationModel: LocationModel? = null,
    val places: List<PlaceModel> = emptyList()
)