package com.example.adventrack.features.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.domain.model.OrderModel
import com.example.adventrack.utils.convertMillisToDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<TransactionViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState get() = _viewState.asStateFlow()
    fun processEvent(transactionViewEvent: TransactionViewEvent) {
        when (transactionViewEvent) {
            is TransactionViewEvent.SetTicketList -> {
                // only quantity > 0
                val listTicket = transactionViewEvent.listTicket.filter { it.quantity > 0 }
                _viewState.update {
                    it.copy(listTicket = listTicket)
                }
            }

            is TransactionViewEvent.SetTotalPrice -> {
                _viewState.update {
                    it.copy(totalPrice = transactionViewEvent.totalPrice)
                }
            }

            TransactionViewEvent.OnPay -> {
                postOrder()
            }
        }
    }

    private fun postOrder() {
        viewModelScope.launch {
            _viewEffect.emit(TransactionViewEffect.OnLoading)
            try {
                viewState.value.listTicket.forEach {
                    val order = OrderModel(
                        id = null,
                        name = it.name,
                        place = it.address,
                        price = it.price,
                        date = it.timestamp.toLong().convertMillisToDateString(),
                        quantity = it.quantity,
                    )
                    firebaseClient.addOrder(order)
                }
                _viewEffect.emit(TransactionViewEffect.OnSuccess("Success"))

            } catch (e: Exception) {
                Log.d("TransactionViewModel", "postOrder: ${e.message}")
                _viewEffect.emit(TransactionViewEffect.OnError(e.message.toString()))
            }

        }
    }
}