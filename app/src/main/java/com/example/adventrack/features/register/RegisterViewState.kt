package com.example.adventrack.features.register

sealed class RegisterViewState {
    object Idle : RegisterViewState()
    object Loading : RegisterViewState()
    object Success : RegisterViewState()
    data class AuthError(val message: String) : RegisterViewState()
}