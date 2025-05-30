package com.example.jetpack_api_call_demo.model.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddClientRequest(
    @Json(name = "amount")
    val amount: Any?,
    @Json(name = "client_id")
    val clientId: Int?,
    @Json(name = "collection_date")
    val collectionDate: Any?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "issue_date")
    val issueDate: Any?,
    @Json(name = "mobile")
    val mobile: String?,
    @Json(name = "user_name")
    val userName: String?
)