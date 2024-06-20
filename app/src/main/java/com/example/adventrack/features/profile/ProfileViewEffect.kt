package com.example.adventrack.features.profile

import com.example.adventrack.features.home.HomeViewEffect

sealed class ProfileViewEffect {
    data object OnLoading : ProfileViewEffect()
    data class OnError(val message: String) : ProfileViewEffect()
    data class OnSuccess(val message: String) : ProfileViewEffect()
}