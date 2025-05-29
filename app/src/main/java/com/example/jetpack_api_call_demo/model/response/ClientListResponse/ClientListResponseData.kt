package com.jetpack_demo.model.response.ClientListResponse


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClientListResponseData(
    @Json(name = "client_list")
    val clientList: List<ClientListData?>?,
    @Json(name = "total_client")
    val totalClient: Int?
)