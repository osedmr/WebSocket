package com.inohom.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inohom.test.data.model.AuthenticateResponse
import com.inohom.test.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authResult = MutableLiveData<AuthenticateResponse>()
    val authResult: LiveData<AuthenticateResponse> = _authResult

    fun authenticate() {
        repository.connectAndAuthenticate("demo", "123456") { response ->
            _authResult.postValue(response)
        }
    }
}