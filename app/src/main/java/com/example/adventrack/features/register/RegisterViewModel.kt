package com.example.adventrack.features.register

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.features.login.LoginViewEffect
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class RegisterViewModel: ViewModel() {
    private val _viewEffect = MutableSharedFlow<RegisterViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    fun processEvent(event: RegisterViewEvent) {
        when (event) {
            is RegisterViewEvent.Register -> register(event.name, event.email, event.password, event.confirmPassword)
        }
    }

    private fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (!isEmailValid(email)) {
            viewModelScope.launch {
                _viewEffect.emit(RegisterViewEffect.AuthError("Invalid email"))
            }
            return
        }
        if (password != confirmPassword) {
            viewModelScope.launch {
                _viewEffect.emit(RegisterViewEffect.AuthError("Passwords do not match"))
            }
            return
        }
        viewModelScope.launch {
            _viewEffect.emit(RegisterViewEffect.OnLoading)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val stringToUri = "https://picsum.photos/200/300"
                        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(stringToUri.toUri())
                            .build()
                        user?.updateProfile(profileUpdates)
                        Log.d(TAG,"Email signup is successful")
                        viewModelScope.launch {
                            delay(1000)
                            _viewEffect.emit(RegisterViewEffect.OnSuccess("Signup successful"))
                        }
                    } else {
                        task.exception?.let {
                            Log.i(TAG,"Email signup failed with error ${it.localizedMessage}")
                            viewModelScope.launch {
                                _viewEffect.emit(RegisterViewEffect.AuthError(it.localizedMessage ?: "Unknown error"))
                            }
                        }
                    }
                }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }

}