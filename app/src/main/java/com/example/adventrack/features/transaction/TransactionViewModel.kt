package com.example.adventrack.features.transaction

import com.example.adventrack.domain.model.OrderModel
import com.example.adventrack.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TransactionViewModel @Inject constructor(
    private val transactionRepository: LocationRepository
){

    private val _viewEffect = MutableSharedFlow<TransactionViewEffect>()
    val viewEffect get() = _viewEffect

    private val _transactionState = MutableStateFlow(
        OrderModel(
            id = 0,
            name = "",
            place = "",
            quantity = 0,
            price = 0.0,
            date = "",
        )
    )

    fun transaction() {
        TODO("Not yet implemented")
    }
}