package com.example.adventrack.features.home

import com.example.adventrack.domain.model.LocationModel

data class HomeViewState (
    val listLocation: List<LocationModel> = emptyList()
)