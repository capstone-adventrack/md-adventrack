package com.example.adventrack.domain.model

data class EntryTicketModel(
    val name: String,
    val description: String,
    val price: String,
    val quantity: Int,
    val type: String,
)