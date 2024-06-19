package com.example.adventrack.features.register

import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    fun processEvent(event: RegisterViewEvent) {
        when (event) {
            is RegisterViewEvent.Register -> register(event.name, event.email, event.password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        TODO("Not yet implemented")
    }

}