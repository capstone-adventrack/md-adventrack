package com.example.adventrack.features.detail

import androidx.lifecycle.ViewModel
import com.example.adventrack.domain.model.LocationModel
import com.example.adventrack.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: LocationRepository
): ViewModel() {
    private val _viewEffect = MutableSharedFlow<DetailViewEffect>()
    val viewEffect get() = _viewEffect

    private val _locationState = MutableStateFlow(
        LocationModel(
            name = "",
            state = "",
            country = "",
        )
    )

    val locationState get() = _locationState

    fun getDetailLocation(latitude: Double, longitude: Double) {
        TODO("Not yet implemented")
    }
}