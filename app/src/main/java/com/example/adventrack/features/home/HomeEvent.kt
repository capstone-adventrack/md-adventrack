package com.example.adventrack.features.home

sealed class HomeEvent {
    data class GetPlaces(val query: String) : HomeEvent()
}