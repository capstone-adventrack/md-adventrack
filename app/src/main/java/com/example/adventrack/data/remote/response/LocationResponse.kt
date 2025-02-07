package com.example.adventrack.data.remote.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("state")
	val state: String? = null
)
