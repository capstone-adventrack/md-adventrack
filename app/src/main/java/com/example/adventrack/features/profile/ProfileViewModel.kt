package com.example.adventrack.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.domain.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient,
): ViewModel() {
    private val _viewEffect = MutableSharedFlow<ProfileViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(ProfileViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getUserData()
    }

    fun processEvent(event: ProfileViewEvent) {
        when (event) {
            is ProfileViewEvent.GetUser -> {
                getUserData()
            }
            is ProfileViewEvent.OnRefresh -> {
                getUserData()
            }
            is ProfileViewEvent.Logout -> {
                logout()
            }
        }
    }

    private fun getUserData(){
        viewModelScope.launch {
            _viewEffect.emit(ProfileViewEffect.OnLoading)
            val username = firebaseClient.getUsername()
            val imageUrl = firebaseClient.getImageUrl()
            val email = firebaseClient.getEmail()
            _viewState.update {
                it.copy(user = UserModel(name = username.orEmpty(), email = email.orEmpty(), imageUrl = imageUrl.toString()))
            }
            _viewEffect.emit(ProfileViewEffect.OnSuccess("Success"))
        }
    }

    private fun logout(){
        viewModelScope.launch {
            firebaseClient.logout()
            _viewEffect.emit(ProfileViewEffect.OnSuccess("Logout successful"))
        }
    }
}