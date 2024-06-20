package com.example.adventrack.di

import com.example.adventrack.data.remote.network.LocationService
import com.example.adventrack.data.repository.LocationRepositoryImpl
import com.example.adventrack.domain.repository.LocationRepository
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
}