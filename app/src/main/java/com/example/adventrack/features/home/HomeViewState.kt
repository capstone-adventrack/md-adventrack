package com.example.adventrack.features.home

import android.location.Location
import com.example.adventrack.domain.model.LocationModel

data class HomeViewState (
    val location: Location? = null,
    val locationModel: LocationModel? = null,
)