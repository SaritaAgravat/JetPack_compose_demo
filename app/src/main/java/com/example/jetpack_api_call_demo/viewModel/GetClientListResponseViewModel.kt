package com.jetpack_demo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack_demo.base_api.Resource
import com.jetpack_demo.model.request.ClientListRequest
import com.jetpack_demo.model.response.ClientListResponse.ClientListResponse
import com.jetpack_demo.repo.ClientListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetClientListResponseViewModel @Inject constructor(
    private val clientListRepository : ClientListRepository
): ViewModel(){

    private val _dataClientListState: MutableLiveData<Resource<ClientListResponse>> = MutableLiveData()

    val dataState: LiveData<Resource<ClientListResponse>>
        get() = _dataClientListState

    fun setStateEvent(getConfigDataStateEvent: GetClientListResponseViewModelStateEvent.GetClientListEvent, resendOtpRequest: ClientListRequest) {
        _dataClientListState.value = Resource.Loading
        viewModelScope.launch {
            when (getConfigDataStateEvent) {
                is GetClientListResponseViewModelStateEvent.GetClientListEvent -> {
                    clientListRepository.fetchClientList(resendOtpRequest)
                        .collect { dataState ->
                            _dataClientListState.value = dataState
                        }
                }

                else -> {}
            }
        }
    }

    sealed class GetClientListResponseViewModelStateEvent {
        object GetClientListEvent : GetClientListResponseViewModelStateEvent()


    }
}

