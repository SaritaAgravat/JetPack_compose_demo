package  com.jetpack_demo.base_api

import com.jetpack_demo.model.response.ClientListResponse.ClientListResponse
import dagger.hilt.InstallIn
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiServices {

    @POST("getclientlist")
    suspend fun getClientListResponse(@Body request: RequestBody): Response<ClientListResponse>
}