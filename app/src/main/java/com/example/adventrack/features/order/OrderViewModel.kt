package com.example.adventrack.features.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.data.remote.mapper.toOrderModel
import com.example.adventrack.domain.model.OrderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firebaseClient: FirebaseClient,
) : ViewModel() {
    private val _viewEffect = MutableSharedFlow<OrderViewEffect>()
    val viewEffect get() = _viewEffect.asSharedFlow()
    private val _viewState = MutableStateFlow(OrderViewState())
    val viewState get() = _viewState.asStateFlow()

    init {
        getOrdersByUserId()
    }

    private fun getOrdersByUserId() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            firebaseClient.getOrdersByUserId()
                .addOnSuccessListener { result ->
                    viewModelScope.launch {
                        val list = mutableListOf<OrderModel>()
                        result.documents.forEach {
                            val data = it.toOrderModel()
                            list.add(data)
                        }
                        delay(1000)
                        _viewState.update {
                            it.copy(
                                listOrder = list,
                                isLoading = false
                            )
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "TAG",
                        "Error getting documents.",
                        exception
                    )
                    _viewState.update {
                        it.copy(isLoading = false)
                    }
                }
        }
    }
}