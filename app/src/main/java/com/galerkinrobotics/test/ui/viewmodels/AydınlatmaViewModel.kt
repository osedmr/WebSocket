package com.galerkinrobotics.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galerkinrobotics.test.data.model.GetControlListResponse
import com.galerkinrobotics.test.data.model.OnEntityUpdatedResponse
import com.galerkinrobotics.test.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AydınlatmaViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _controlListResult = MutableLiveData<GetControlListResponse>()
    val controlListResult: LiveData<GetControlListResponse> = _controlListResult

    private val _entityUpdatedResult = MutableLiveData<OnEntityUpdatedResponse>()
    val entityUpdatedResult: LiveData<OnEntityUpdatedResponse> = _entityUpdatedResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getControlList() {
        _isLoading.postValue(true)
        repository.getControlList { response ->
            _isLoading.postValue(false)
            _controlListResult.postValue(response)
        }
    }
    
    fun updateControlValue(controlId: String, value: Int) {
        _isLoading.postValue(true)
        repository.updateControlValue(controlId, value) { response ->
            _isLoading.postValue(false)
            _entityUpdatedResult.postValue(response)
        }
    }
}