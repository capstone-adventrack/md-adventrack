package com.example.adventrack.features.profile

sealed class ProfileViewEvent {
    data class GetUser(val userId: String) : ProfileViewEvent()
    data object OnRefresh : ProfileViewEvent()

    data object Logout : ProfileViewEvent()
}