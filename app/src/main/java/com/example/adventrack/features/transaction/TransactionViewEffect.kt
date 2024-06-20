package com.example.adventrack.features.transaction

sealed class TransactionViewEffect {
    data object OnLoading : TransactionViewEffect()
    data class OnSuccess(val message: String) : TransactionViewEffect()
    data class OnError(val message: String) : TransactionViewEffect()
}