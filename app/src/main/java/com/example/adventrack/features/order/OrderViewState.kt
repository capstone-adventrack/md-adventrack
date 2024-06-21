package com.example.adventrack.features.order

import com.example.adventrack.domain.model.OrderModel

data class OrderViewState(
    val isLoading : Boolean = false,
    val listOrder : List<OrderModel> = emptyList(),
)
