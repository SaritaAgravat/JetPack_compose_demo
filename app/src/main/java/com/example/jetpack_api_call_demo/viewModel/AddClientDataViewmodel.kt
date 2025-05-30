package com.example.jetpack_api_call_demo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_api_call_demo.model.request.AddClientRequest
import com.example.jetpack_api_call_demo.model.response.CommonStringResponse
import com.example.jetpack_api_call_demo.repo.AddClientRepository
import com.jetpack_demo.base_api.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddClientDataViewmodel  @Inject constructor(
    private val addClientRepository: AddClientRepository,) : ViewModel()
  {

      //to update verify id
      private val _addUpdateClientDataState: MutableLiveData<Resource<CommonStringResponse>> =
          MutableLiveData()

      val addUpdateClientDataState: LiveData<Resource<CommonStringResponse>>
          get() = _addUpdateClientDataState

      fun addUpdateClientApiDataState(
          addClientDataViewmodelState: AddClientDataViewmodelState,
          request: AddClientRequest,
          profileImage: File?,
      ) {
          _addUpdateClientDataState.value = Resource.Loading
          viewModelScope.launch {
              when (addClientDataViewmodelState) {
                  is AddClientDataViewmodelState.addClientDataViewmodelState -> {
                      addClientRepository.addEditClientData(request, profileImage)
                          .collect { dataState ->
                              _addUpdateClientDataState.value = dataState
                          }
                  }

                  else -> {}
              }
          }
      }


      sealed class AddClientDataViewmodelState {
          object addClientDataViewmodelState : AddClientDataViewmodelState()

      }
}