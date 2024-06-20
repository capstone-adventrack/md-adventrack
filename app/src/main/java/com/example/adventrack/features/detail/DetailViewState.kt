package com.example.adventrack.features.detail

import com.example.adventrack.domain.model.EntryTicketModel
import com.example.adventrack.domain.model.PlaceModel

data class DetailViewState(
    val placeModel: PlaceModel? = null,
    val entryTicketList: List<EntryTicketModel> = emptyList(),
    val totalPrice: Int = 0
)