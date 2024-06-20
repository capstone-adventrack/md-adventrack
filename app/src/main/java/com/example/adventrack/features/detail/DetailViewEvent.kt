package com.example.adventrack.features.detail

import com.example.adventrack.domain.model.EntryTicketModel

sealed class DetailViewEvent {
    data class GetPlaceById(val placeId: String) : DetailViewEvent()
    data class GetEntryTicketList(val listEntryTicket: List<EntryTicketModel>) : DetailViewEvent()
    data class OnAddEntryTicket(val type: String) : DetailViewEvent()
    data class OnMinusEntryTicket(val type: String) : DetailViewEvent()
}