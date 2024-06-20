package com.example.adventrack.features.login

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.features.register.RegisterViewModel
import com.example.adventrack.features.register.RegisterViewState
import com.google.firebase.auth.FirebaseAuth
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

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _viewEffect.emit(LoginViewEffect.OnLoading)
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Login is successful")
                        _viewEffect.tryEmit(LoginViewEffect.NavigateToHome)
                        viewModelScope.launch {
                            delay(1000)
                            _viewEffect.emit(LoginViewEffect.OnSuccess("Login successful"))
                        }
                    } else {
                        task.exception?.let {
                            Log.i(TAG, "Login failed with error ${it.localizedMessage}")
                            _viewEffect.tryEmit(LoginViewEffect.AuthError(it.localizedMessage ?: "Unknown error"))
                        }
                    }
                }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

}