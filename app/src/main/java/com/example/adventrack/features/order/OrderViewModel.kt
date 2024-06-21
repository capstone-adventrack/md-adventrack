package com.example.adventrack.features.order

import com.example.adventrack.domain.model.OrderModel
import com.example.adventrack.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val orderRepository: LocationRepository
) {
    private val _viewEffect = MutableSharedFlow<OrderViewEffect>()
    val viewEffect get() = _viewEffect

    fun order() {
        TODO("Not yet implemented")
    }
}