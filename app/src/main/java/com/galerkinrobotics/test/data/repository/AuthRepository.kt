package com.galerkinrobotics.test.data.repository

import com.galerkinrobotics.test.data.model.AuthenticateResponse
import com.galerkinrobotics.test.data.model.GetControlListResponse
import com.galerkinrobotics.test.data.model.OnEntityUpdatedResponse
import com.galerkinrobotics.test.websocket.WebSocketManager
import javax.inject.Inject

class AuthRepository @Inject constructor(private val wsManager: WebSocketManager
) {
    fun connectAndAuthenticate(username: String, password: String, onResult: (AuthenticateResponse) -> Unit
    ) {
        wsManager.connect(onResult)
        wsManager.sendAuthenticateRequest(username, password)
    }
    
    fun getControlList(onResult: (GetControlListResponse) -> Unit) {
        wsManager.setControlListListener(onResult)
        wsManager.sendGetControlListRequest()
    }
    
    fun updateControlValue(controlId: String, value: Int, onResult: (OnEntityUpdatedResponse) -> Unit) {
        wsManager.setEntityUpdatedListener(onResult)
        wsManager.sendUpdateControlValueRequest(controlId, value)
    }
}
