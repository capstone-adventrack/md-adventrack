package com.example.adventrack.features.register

sealed class RegisterViewEvent {
    data class Register(val name: String, val email: String, val password: String) :
        RegisterViewEvent()
}