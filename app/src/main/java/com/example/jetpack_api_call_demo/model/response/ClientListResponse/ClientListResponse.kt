package com.jetpack_demo.model.response.ClientListResponse


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClientListResponse(
    @Json(name = "data")
    val data: ClientListResponseData?
)