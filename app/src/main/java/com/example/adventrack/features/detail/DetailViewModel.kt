package com.example.adventrack.features.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.data.remote.mapper.toPlaceModel
import com.example.adventrack.utils.stringIDRToInteger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<DetailViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()
    private val _viewState = MutableStateFlow(DetailViewState())
    val viewState get() = _viewState.asStateFlow()

    fun processEvent(event: DetailViewEvent) {
        when (event) {
            is DetailViewEvent.GetPlaceById -> {
                getPlaceById(event.placeId)
            }

            is DetailViewEvent.GetEntryTicketList -> {
                _viewState.update { it.copy(entryTicketList = event.listEntryTicket) }
            }

            is DetailViewEvent.OnAddEntryTicket -> {
                // Increment quantity
                val entryTicketList = _viewState.value.entryTicketList.map {
                    if (it.type == event.type) {
                        //count total price
                        it.copy(quantity = it.quantity + 1)
                    } else {
                        it
                    }
                }
                _viewState.update {
                    it.copy(
                        entryTicketList = entryTicketList,
                        totalPrice = entryTicketList.sumOf { ticket ->
                            val price = stringIDRToInteger(ticket.price)
                            (ticket.quantity * price)
                        }
                    )
                }
            }

            is DetailViewEvent.OnMinusEntryTicket -> {
                // Decrement quantity
                val entryTicketList = _viewState.value.entryTicketList.map {
                    if (it.type == event.type && it.quantity != 0) {
                        it.copy(quantity = it.quantity - 1)
                    } else {
                        it
                    }
                }
                _viewState.update {
                    it.copy(
                        entryTicketList = entryTicketList,
                        totalPrice = entryTicketList.sumOf { ticket ->
                            val price = stringIDRToInteger(ticket.price)
                            (ticket.quantity * price)
                        })
                }
            }

            DetailViewEvent.OnRefresh -> {
                getPlaceById(_viewState.value.placeModel?.id.orEmpty())
            }
        }
    }

    private fun getPlaceById(placeId: String) {
        viewModelScope.launch {
            _viewEffect.emit(DetailViewEffect.OnLoading)
            val response = firebaseClient.getPlaceById(placeId)
                .addOnSuccessListener { result ->
                    val data = result.toPlaceModel()
                    _viewState.update { it.copy(placeModel = data) }
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "TAG",
                        "Error getting documents.",
                        exception
                    )
                }
            if (response.isComplete) {
                _viewEffect.emit(DetailViewEffect.OnSuccess("Success"))
            } else {
                _viewEffect.emit(DetailViewEffect.OnError("Error"))
            }
        }
    }
}