package com.example.jetpack_api_call_demo.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonStringResponse(
    @Json(name = "message")
    val message: String?
)