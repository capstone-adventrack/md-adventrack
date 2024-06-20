package com.example.adventrack.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.data.Result
import com.example.adventrack.data.remote.mapper.toFirstCity
import com.example.adventrack.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<HomeViewEffect>()
    val viewEffect get() = _viewEffect

    private val _listPlaces = MutableStateFlow(
        HomeViewState()
    )
    val listPlaces get() = _listPlaces.asSharedFlow()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmptyPlace = MutableLiveData<Boolean>()
    val isEmptyPlaces: LiveData<Boolean> = _isEmptyPlace

    fun processEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetPlaces -> getPlaces(
                event.query
            )
        }
    }

    private fun getPlaces(
        city: String
    ) {

    }
}