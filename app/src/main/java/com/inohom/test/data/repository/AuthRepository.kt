package com.inohom.test.data.repository

import com.inohom.test.data.model.AuthenticateResponse
import com.inohom.test.data.model.GetControlListResponse
import com.inohom.test.data.model.OnEntityUpdatedResponse
import com.inohom.test.websocket.WebSocketManager
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
