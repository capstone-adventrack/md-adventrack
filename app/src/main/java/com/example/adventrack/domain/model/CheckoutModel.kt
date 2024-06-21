package com.example.adventrack.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutModel(
    val listTicket : List<EntryTicketModel>,
    val totalPrice : Int
) : Parcelable
