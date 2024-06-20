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
import com.example.adventrack.data.remote.mapper.toFirstCity
import com.example.adventrack.domain.model.LocationModel
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState get() = _viewState.asStateFlow()

    fun processEvent(event: HomeViewEvent) {
        when (event) {
            is HomeViewEvent.GetLocation -> {
                getLastLocation(event.location)
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
}