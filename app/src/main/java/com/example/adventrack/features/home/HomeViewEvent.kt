package com.example.adventrack.features.home

import android.location.Location

sealed class HomeViewEvent {
    data class GetLocation(val location: Location) : HomeViewEvent()
    data object GetPlaces : HomeViewEvent()
}