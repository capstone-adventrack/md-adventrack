package com.example.adventrack.features.transaction

import com.example.adventrack.domain.model.EntryTicketModel

sealed class TransactionViewEvent{
    data class SetTicketList(val listTicket: List<EntryTicketModel>) : TransactionViewEvent()
    data class SetTotalPrice(val totalPrice: Int) : TransactionViewEvent()
    data object OnPay : TransactionViewEvent()
}
