package com.example.adventrack.features.login

sealed class LoginViewEvent {
    data class Login(val username: String, val password: String) : LoginViewEvent()
    object GoogleSignIn : LoginViewEvent()
}