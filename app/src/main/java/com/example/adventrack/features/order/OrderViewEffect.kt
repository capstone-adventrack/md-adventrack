package com.example.adventrack.features.order

sealed class OrderViewEffect {
    data object OnLoading : OrderViewEffect()
    data class OnError(val message: String) : OrderViewEffect()
    data class OnSuccess(val message: String) : OrderViewEffect()
}