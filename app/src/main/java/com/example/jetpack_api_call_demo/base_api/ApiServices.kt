package  com.jetpack_demo.base_api

import com.example.jetpack_api_call_demo.model.response.CommonStringResponse
import com.jetpack_demo.model.response.ClientListResponse.ClientListResponse
import dagger.hilt.InstallIn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiServices {

    @POST("getclientlist")
    suspend fun getClientListResponse(@Body request: RequestBody): Response<ClientListResponse>

    @Multipart
    @POST("addupdateclient")
    suspend fun addupdateclient(
        @Part("client_id") clientId: RequestBody?,
        @Part("user_name") userName: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("amount") amount: RequestBody?,
        @Part("issue_date") issueDate: RequestBody?,
        @Part("collection_date") collectionDate: RequestBody?,
        @Part image: MultipartBody.Part?,
    ): Response<CommonStringResponse>
}