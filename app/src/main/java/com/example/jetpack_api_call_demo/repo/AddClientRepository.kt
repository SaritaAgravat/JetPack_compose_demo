package com.example.jetpack_api_call_demo.repo

import com.example.jetpack_api_call_demo.model.request.AddClientRequest
import com.example.jetpack_api_call_demo.model.response.CommonStringResponse
import com.jetpack_demo.base_api.ApiServices
import com.jetpack_demo.base_api.ErrorParseHandler
import com.jetpack_demo.base_api.Resource
import com.jetpack_demo.util.logD
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddClientRepository(private val apiService: ApiServices, private val moshi: Moshi, ) {
    suspend fun addEditClientData(
        request: AddClientRequest,
        profileImage: File?
    ): Flow<Resource<CommonStringResponse>> =
        flow {
            try {
                val contentType = "application/json; charset=utf-8"


                val requstClientData = request
                val clientId = request.clientId?.takeIf { it != 0 }?.toString()
                    ?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val userName =
                    requstClientData.userName.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val mobile =
                    requstClientData.mobile.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val amount =
                    requstClientData.amount.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val issueDate =
                    requstClientData.issueDate.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val collectionDate =
                    requstClientData.collectionDate.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


                var profilePart: MultipartBody.Part? = null
                if (profileImage != null) {
                    val requestFile: RequestBody =
                        profileImage.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    profilePart =
                        MultipartBody.Part.createFormData(
                            "image",
                            profileImage.name,
                            requestFile
                        )
                }
                val apiResponse = apiService.addupdateclient(
                    clientId,
                    userName,
                    mobile,
                    amount,
                    issueDate,
                    collectionDate,
                    profilePart
                )
                if (apiResponse.isSuccessful) {
                    val response = apiResponse.body()!!
                    if (response != null) {
                        logD("CheckConfigResponse", "${response!!}")
                        if (apiResponse.code() == 200) {
                            try {
                                // logD("CheckConfigResponseIns", "Login Response ${response.message}")
                                emit(Resource.Success(response))
                            } catch (e: Exception) {
                                logD(
                                    "CheckConfigDataExceptionIn200",
                                    e.localizedMessage ?: "Exception"
                                )
                                // emit(Resource.Error("Config Data Not Available"))
                            }
                        }
                    }

                } else if (apiResponse.code() == 401) {
                    val errorBody = apiResponse.errorBody()?.string()
                    val errorMessage = ErrorParseHandler.parseErrorMessage(errorBody)
                    logD("CheckConfigResponseIns", "Login Response $errorMessage")
                    emit(Resource.Error(errorMessage.toString(), 401))
                } else {
                    val errorBody = apiResponse.errorBody()?.string()
                    val errorMessage = ErrorParseHandler.parseErrorMessage(errorBody)
                    logD("CheckConfigResponseIns", "Login Response $errorMessage")
                    emit(Resource.Error(errorMessage.toString()))
                }
            } catch (e: Exception) {
                logD(
                    "CheckConfigDataExceptionInCatch",
                    e.localizedMessage ?: "Login Response Exception"
                )
                e.localizedMessage?.let { Resource.Error(it.toString()) }?.let { emit(it) }
            }
        }


}