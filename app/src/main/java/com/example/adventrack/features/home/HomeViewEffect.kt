package com.example.adventrack.features.home

sealed class HomeViewEffect {
    data object OnLoading : HomeViewEffect()
    data class OnError(val message: String) : HomeViewEffect()
    data class OnSuccess(val message: String) : HomeViewEffect()
}