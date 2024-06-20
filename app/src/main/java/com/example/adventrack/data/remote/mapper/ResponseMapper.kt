package com.example.adventrack.data.remote.mapper

import com.example.adventrack.data.remote.response.LocationResponse
import com.example.adventrack.domain.model.LocationModel

fun LocationResponse.toFirstCity() = LocationModel(
    country = country,
    name= name,
    state = state
)