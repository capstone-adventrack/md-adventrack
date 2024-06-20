package com.example.adventrack.data.repository

import com.example.adventrack.data.remote.network.LocationService
import com.example.adventrack.data.remote.response.LocationErrorResponse
import com.example.adventrack.data.remote.response.LocationResponse
import com.example.adventrack.domain.repository.LocationRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.adventrack.data.Result
import kotlinx.coroutines.flow.onStart

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService
) : LocationRepository {
    override suspend fun getLocation(
        latitude: Double,
        longitude: Double
    ): Flow<Result<LocationResponse>> {
        return flow {
            try {
                val response = locationService.getCity(latitude, longitude)
                if (response.isSuccessful) {
                    emit(Result.Success(response.body()?.first() ?: LocationResponse()))
                } else {
                    val errorBody = response.errorBody()
                    val apiError =
                        Gson().fromJson(
                            errorBody?.string() ?: "",
                            LocationErrorResponse::class.java
                        )
                    emit(Result.Error(apiError.error ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Unknown error"))
            }
        }.onStart { emit(Result.Loading) }
    }
}