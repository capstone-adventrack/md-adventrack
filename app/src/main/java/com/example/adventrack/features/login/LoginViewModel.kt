package com.example.adventrack.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient
): ViewModel(){
    private val _viewEffect = MutableSharedFlow<LoginViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()



    fun onEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.Login -> {
                login(
                    event.username,
                    event.password
                )
            }
            is LoginViewEvent.GoogleSignIn -> {

            }
            is LoginViewEvent.NavigateToHome -> {
            }
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _viewEffect.emit(LoginViewEffect.OnLoading)
            // Dummy user login
            if (username == "admin" && password == "admin") {
                //Wait 2 seconds to simulate network request
                delay(2000)
                _viewEffect.emit(LoginViewEffect.OnSuccess("Login successful"))
            } else {
                _viewEffect.emit(LoginViewEffect.OnError("Invalid credentials"))
            }
        }
    }
}