package com.example.adventrack.data.remote.network

import com.example.adventrack.data.remote.response.LocationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET(NetworkConstant.LOCATION)
    suspend fun getCity(
        @Query("lat") page: Double?,
        @Query("lon") size: Double?,
    ): Response<List<LocationResponse>>
}