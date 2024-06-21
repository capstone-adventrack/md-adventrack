package com.example.adventrack.data.firebase

import com.example.adventrack.data.firebase.FirebaseConstant.COLLECTION_ORDERS
import com.example.adventrack.data.firebase.FirebaseConstant.COLLECTION_PLACES
import com.example.adventrack.data.remote.mapper.toDocs
import com.example.adventrack.domain.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseClient @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    fun getNearbyPlaces() = firestore.collection(COLLECTION_PLACES).get()
    fun getNearbyPlacesByCity(city: String) = firestore.collection(COLLECTION_PLACES).whereEqualTo("city", city).get()
    fun getHighestRatedPlaces() = firestore.collection(COLLECTION_PLACES).orderBy("rating").get()
    fun getPlaceById(placeId: String) = firestore.collection(COLLECTION_PLACES).document(placeId).get()

    fun getUsername() = firebaseAuth.currentUser?.displayName
    fun getImageUrl() = firebaseAuth.currentUser?.photoUrl
    fun getEmail() = firebaseAuth.currentUser?.email

    fun logout() = firebaseAuth.signOut()

    fun addOrder(order: OrderModel) = firestore.collection(COLLECTION_ORDERS).add(order.toDocs(
        firebaseAuth.currentUser?.uid.orEmpty()
    ))

    fun getOrdersByUserId() = firestore.collection(COLLECTION_ORDERS).whereEqualTo("user_id", firebaseAuth.currentUser?.uid).get()
}