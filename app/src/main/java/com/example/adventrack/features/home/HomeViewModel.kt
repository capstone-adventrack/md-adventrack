package com.example.adventrack.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adventrack.domain.model.PlaceModel
import com.example.adventrack.domain.repository.LocationRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val LocationRepository: LocationRepository
) : ViewModel() {
    private val _listPlaces = MutableLiveData<List<PlaceModel>>()
    val listPlaces: LiveData<List<PlaceModel>> = _listPlaces

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEmptyPlace = MutableLiveData<Boolean>()
    val isEmptyPlaces: LiveData<Boolean> = _isEmptyPlace

    fun processEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.GetPlaces -> getPlaces()
        }
    }

    private fun getPlaces() {
        TODO("Not yet implemented")
    }
}