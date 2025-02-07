package com.example.adventrack.features.login

sealed class LoginViewEffect {
    data object OnLoading : LoginViewEffect()
    data class OnSuccess(val message: String) : LoginViewEffect()
    data class OnError(val message: String) : LoginViewEffect()

    object NavigateToHome : LoginViewEffect()

    data class AuthError(val message: String) : LoginViewEffect()
}