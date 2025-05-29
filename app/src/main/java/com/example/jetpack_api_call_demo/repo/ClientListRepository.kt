package com.jetpack_demo.repo

import android.util.Log
import com.jetpack_demo.base_api.ApiServices
import com.jetpack_demo.base_api.ErrorParseHandler
import com.jetpack_demo.base_api.Resource
import com.jetpack_demo.model.request.ClientListRequest
import com.jetpack_demo.model.response.ClientListResponse.ClientListResponse


import com.jetpack_demo.util.logD
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ClientListRepository(private val apiService: ApiServices,
                           private val moshi: Moshi
) {
    suspend fun fetchClientList(resendOtpRequest: ClientListRequest): Flow<Resource<ClientListResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"
                val requestBody =
                    getRequestBody(resendOtpRequest).toRequestBody(contentType.toMediaTypeOrNull())

                val apiResponse = apiService.getClientListResponse(requestBody)

                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()
                    if (response != null) {
                        // Response body is not null
                        logD("CheckConfigResponse", "$response")
                        if (apiResponse.code() == 200) {
                            try {
                                logD("CheckConfigResponseIns", "ResendOtp Response ${response.data}")
                                emit(Resource.Success(response))
                            } catch (e: Exception) {
                                logD("CheckConfigDataExceptionIn200", e.localizedMessage ?: "ResendOtp Exception")
                                emit(Resource.Error("Config Data Not Available"))
                            }
                        }
                    } else {
                        // Response body is null
                        logD("CheckConfigResponse", "Response body is null")
                        emit(Resource.Error("Response body is null"))
                    }
                }
                else if(apiResponse.code() == 401){
                    val errorBody = apiResponse.errorBody()?.string()
                    val errorMessage = ErrorParseHandler.parseErrorMessage(errorBody)
                    logD("CheckConfigResponseIns", "ResendOtp Response $errorMessage")
                    emit(Resource.Error(errorMessage.toString(),401))
                }
                else{
                    val errorBody = apiResponse.errorBody()?.string()
                    val errorMessage = ErrorParseHandler.parseErrorMessage(errorBody)
                    logD("CheckConfigResponseIns", "ResendOtp Response $errorMessage")
                    emit(Resource.Error(errorMessage.toString()))
                }
            }
            catch(e: Exception){
                logD("CheckConfigDataExceptionInCatch", e.localizedMessage ?: "ResendOtp Exception")
                e.localizedMessage?.let { Resource.Error(it.toString()) }?.let { emit(it) }
            }
        }

    private fun getRequestBody(resendOtpRequest: ClientListRequest): String {
        val adapter = moshi.adapter(ClientListRequest::class.java)
        val json = adapter.toJson(resendOtpRequest)
        Log.e("CheckConfigRequest", "ResendOtp getRequestBody: $json")
        return json
    }
}