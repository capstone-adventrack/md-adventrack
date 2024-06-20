package com.example.adventrack.domain.repository

import com.example.adventrack.data.remote.response.LocationResponse
import kotlinx.coroutines.flow.Flow
import com.example.adventrack.data.Result

interface LocationRepository {
    suspend fun getLocation(latitude: Double, longitude: Double): Flow<Result<LocationResponse>>
}