package com.example.adventrack.data.firebase

import com.example.adventrack.data.firebase.FirebaseConstant.COLLECTION_PLACES
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getNearbyPlaces() = firestore.collection(COLLECTION_PLACES).get()
    fun getNearbyPlacesByCity(city: String) = firestore.collection(COLLECTION_PLACES).whereEqualTo("city", city).get()
}