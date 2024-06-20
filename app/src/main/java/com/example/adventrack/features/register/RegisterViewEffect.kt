package com.example.adventrack.features.register

sealed class RegisterViewEffect {
    data object OnLoading : RegisterViewEffect()
    data class OnSuccess(val message: String) : RegisterViewEffect()
    data class OnError(val message: String) : RegisterViewEffect()

    data class AuthError(val message: String) : RegisterViewEffect()
}