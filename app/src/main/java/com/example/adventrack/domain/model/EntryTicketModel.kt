package com.example.adventrack.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryTicketModel(
    val name: String,
    val description: String,
    val price: String,
    val quantity: Int,
    val type: String,
) : Parcelable