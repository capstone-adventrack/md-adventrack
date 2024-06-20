package com.example.adventrack.di

import com.example.adventrack.data.firebase.FirebaseClient
import com.example.adventrack.data.remote.network.LocationService
import com.example.adventrack.data.repository.LocationRepositoryImpl
import com.example.adventrack.domain.repository.LocationRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepostitoryModule {
    @Provides
    @Singleton
    fun provideAppRepository(locationService: LocationService): LocationRepository {
        return LocationRepositoryImpl(locationService)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseClient(firestore: FirebaseFirestore,firebaseAuth: FirebaseAuth): FirebaseClient {
        return FirebaseClient(firestore,firebaseAuth)
    }
}