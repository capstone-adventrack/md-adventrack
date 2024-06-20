package com.example.adventrack.data.remote.response

import com.google.gson.annotations.SerializedName

data class LocationErrorResponse(

	@field:SerializedName("error")
	val error: String? = null
)
