package com.inohom.test.websocket

import android.util.Log
import com.inohom.test.data.model.AuthParam
import com.inohom.test.data.model.AuthenticateRequest
import com.inohom.test.data.model.AuthenticateResponse
import com.inohom.test.data.model.GetControlListRequest
import com.inohom.test.data.model.GetControlListResponse
import com.inohom.test.data.model.UpdateControlValueRequest
import com.inohom.test.data.model.UpdateControlParam
import com.inohom.test.data.model.OnEntityUpdatedResponse
import com.inohom.test.util.Constants
import com.google.gson.Gson

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Singleton
class WebSocketManager @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) {

    private var webSocket: WebSocket? = null

    private var authListener: ((AuthenticateResponse) -> Unit)? = null
    private var controlListListener: ((GetControlListResponse) -> Unit)? = null
    private var entityUpdatedListener: ((OnEntityUpdatedResponse) -> Unit)? = null
    private var isConnected = false
    private var pendingMessage: String? = null

    fun connect(onAuthReceived: (AuthenticateResponse) -> Unit) {
        val request = Request.Builder()
            // .url("wss://echo.websocket.org")  // Test için public WebSocket server (WSS)
            .url(Constants.BASE_URL)  // Orijinal sunucu
            .build()

        this.authListener = onAuthReceived
        isConnected = false

        Log.d("WebSocket", "Bağlantı kuruluyor: URL...")

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Bağlantı başarılı! Response: ${response.code}")
                isConnected = true
                
                // Bekleyen mesajı gönder
                pendingMessage?.let { message ->
                    Log.d("WebSocket", "Bekleyen mesaj gönderiliyor: $message")
                    webSocket.send(message)
                    pendingMessage = null
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Mesaj alındı: $text")
                
                // String mesajları (hoş geldin vb.) ignore et
                if (!text.startsWith("{")) {
                    Log.d("WebSocket", "String mesaj ignore edildi: $text")
                    return
                }
                
                try {
                    // Önce mesaj türünü anlamak için genel parsing yap
                    val jsonObject = gson.fromJson(text, com.google.gson.JsonObject::class.java)
                    val method = jsonObject.get("method")?.asString
                    
                    when (method) {
                        "Authenticate" -> {
                            val response = gson.fromJson(text, AuthenticateResponse::class.java)
                            Log.d("WebSocket", "Auth response parse başarülı: $response")
                            authListener?.invoke(response)
                        }
                        "GetControlList" -> {
                            val response = gson.fromJson(text, GetControlListResponse::class.java)
                            Log.d("WebSocket", "ControlList response parse başarılı: $response")
                            controlListListener?.invoke(response)
                        }
                        "OnEntityUpdated" -> {
                            val response = gson.fromJson(text, OnEntityUpdatedResponse::class.java)
                            Log.d("WebSocket", "EntityUpdated response parse başarılı: $response")
                            entityUpdatedListener?.invoke(response)
                        }
                        else -> {
                            Log.d("WebSocket", "Bilinmeyen method: $method")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "JSON parse error: ${e.message}")
                    Log.e("WebSocket", "Problematik JSON: $text")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Bağlantı hatası: ${t.message}")
                Log.e("WebSocket", "Response code: ${response?.code}")
                Log.e("WebSocket", "Hata detayı: ${t.stackTraceToString()}")
                isConnected = false
                pendingMessage = null
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Bağlantı kapanıyor: $code - $reason")
                isConnected = false
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Bağlantı kapandı: $code - $reason")
                isConnected = false
            }
        })
    }

    fun sendAuthenticateRequest(username: String, password: String) {
        val request = AuthenticateRequest(true,8,params = listOf(AuthParam(username, password)),"Authenticate")
        val json: String = gson.toJson(request)
        Log.d("WebSocket", "Gönderilen Auth JSON: $json")
        
        if (isConnected) {
            Log.d("WebSocket", "Bağlantı aktif, Auth mesajı gönderiliyor")
            webSocket?.send(json)
        } else {
            Log.d("WebSocket", "Bağlantı henüz kurulmadı, Auth mesajı bekletiliyor")
            pendingMessage = json
        }
    }
    
    fun setControlListListener(listener: (GetControlListResponse) -> Unit) {
        this.controlListListener = listener
    }
    
    fun setEntityUpdatedListener(listener: (OnEntityUpdatedResponse) -> Unit) {
        this.entityUpdatedListener = listener
    }
    
    fun sendGetControlListRequest() {
        val request = GetControlListRequest(
            isRequest = true,
            id = 5,
            params = listOf(emptyMap()),
            method = "GetControlList"
        )
        val json: String = gson.toJson(request)
        Log.d("WebSocket", "Gönderilen GetControlList JSON: $json")
        
        if (isConnected) {
            Log.d("WebSocket", "Bağlantı aktif, GetControlList mesajı gönderiliyor")
            webSocket?.send(json)
        } else {
            Log.d("WebSocket", "Bağlantı henüz kurulmadı, GetControlList mesajı bekletiliyor")
            pendingMessage = json
        }
    }
    
    fun sendUpdateControlValueRequest(controlId: String, value: Int) {
        val request = UpdateControlValueRequest(
            isRequest = true,
            id = 84,
            params = listOf(UpdateControlParam(controlId, value)),
            method = "UpdateControlValue"
        )
        val json: String = gson.toJson(request)
        Log.d("WebSocket", "Gönderilen UpdateControlValue JSON: $json")
        
        if (isConnected) {
            Log.d("WebSocket", "Bağlantı aktif, UpdateControlValue mesajı gönderiliyor")
            webSocket?.send(json)
        } else {
            Log.d("WebSocket", "Bağlantı henüz kurulmadı, UpdateControlValue mesajı bekletiliyor")
            pendingMessage = json
        }
    }
    
    fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        isConnected = false
        pendingMessage = null
        authListener = null
        controlListListener = null
        entityUpdatedListener = null
    }
    
    fun isConnected(): Boolean = isConnected
}
