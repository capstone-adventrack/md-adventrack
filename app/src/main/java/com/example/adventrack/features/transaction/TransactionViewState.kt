package com.example.adventrack.features.transaction

import com.example.adventrack.domain.model.CheckoutModel
import com.example.adventrack.domain.model.EntryTicketModel

data class TransactionViewState(
    val listTicket : List<EntryTicketModel> = emptyList(),
    val totalPrice : Int = 0
)
