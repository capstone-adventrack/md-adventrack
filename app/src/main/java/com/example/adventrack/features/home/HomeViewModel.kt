package com.example.adventrack.features.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.adventrack.data.Result
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.data.remote.mapper.toFirstCity
import com.example.adventrack.data.remote.mapper.toPlaceModel
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.model.PlaceModel
import com.example.adventrack.domain.model.UserModel
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val firebaseClient: FirebaseClient,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()
    init {
        getHighRatedPlaces()
        getUserData()
    }

    fun processEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.GetLocation -> {
                getLastLocation(event.location)
            }

            HomeViewEvent.GetPlaces -> {
                getHighRatedPlaces()
            }

            is HomeViewEvent.GetNearbyPlacesByCity -> {
                getNearbyPlacesByCity(event.city)
            }

            HomeViewEvent.OnRefresh -> {
                _viewState.value.location?.let { getLastLocation(it) }
                getUserData()
                getHighRatedPlaces()
                getNearbyPlacesByCity(_viewState.value.locationModel?.name.orEmpty())
            }
        }
    }

    private fun getLastLocation(location: Location) {
        viewModelScope.launch {
            locationRepository.getLocation(location.latitude,location.longitude).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val locationModel: LocationModel = result.data.toFirstCity()
                        _viewState.update {
                            it.copy(locationModel = locationModel, location = location)
                        }
                        _viewEffect.emit(HomeViewEffect.OnSuccess("Success"))
                    }
                    is Result.Error -> {
                        _viewEffect.emit(HomeViewEffect.OnError(result.error))
                    }

                    Result.Loading -> {
                        _viewEffect.emit(HomeViewEffect.OnLoading)
                    }
                }
            }
        }
    }

    private fun getHighRatedPlaces() {
        viewModelScope.launch {
            _viewEffect.emit(HomeViewEffect.OnLoading)
            val response = firebaseClient.getHighestRatedPlaces()
                .addOnSuccessListener { result ->
                    val list = mutableListOf<PlaceModel>()
                    //Take only 5 places
                    result.documents.take(3).forEach {
                        val data = it.toPlaceModel()
                        Log.d("TAG", "getHighRatedPlaces: $data")
                        list.add(data)
                    }
                    _viewState.update {
                        it.copy(places = list)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
            if (response.isComplete) {
                _viewEffect.emit(HomeViewEffect.OnSuccess("Success"))
            } else {
                _viewEffect.emit(HomeViewEffect.OnError("Error"))
            }
        }
    }

    private fun getNearbyPlacesByCity(city: String) {
        viewModelScope.launch {
            _viewEffect.emit(HomeViewEffect.OnLoading)
            val response = firebaseClient.getNearbyPlacesByCity(city)
                .addOnSuccessListener { result ->
                    val list = mutableListOf<PlaceModel>()
                    result.documents.forEach {
                        val data = it.toPlaceModel()
                        list.add(data)
                    }
                    _viewState.update {
                        it.copy(nearbyPlaces = list)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                }
            if (response.isComplete) {
                _viewEffect.emit(HomeViewEffect.OnSuccess("Success"))
            } else {
                _viewEffect.emit(HomeViewEffect.OnError("Error"))
            }
        }
    }

    private fun getUserData(){
        viewModelScope.launch {
            _viewEffect.emit(HomeViewEffect.OnLoading)
            val username = firebaseClient.getUsername()
            val imageUrl = firebaseClient.getImageUrl()
            val email = firebaseClient.getEmail()
            _viewState.update {
                it.copy(user = UserModel(name = username.orEmpty(), email = email.orEmpty(), imageUrl = imageUrl.toString()))
            }
            _viewEffect.emit(HomeViewEffect.OnSuccess("Success"))
        }
    }

}