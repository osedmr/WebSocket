package com.galerkinrobotics.test.websocket

import android.util.Log
import com.galerkinrobotics.test.data.model.AuthParam
import com.galerkinrobotics.test.data.model.AuthenticateRequest
import com.galerkinrobotics.test.data.model.AuthenticateResponse
import com.galerkinrobotics.test.util.Constants
import com.google.gson.Gson

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Singleton
class WebSocketManager @Inject constructor() {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()
    private val gson = Gson()

    private var listener: ((AuthenticateResponse) -> Unit)? = null
    private var isConnected = false
    private var pendingMessage: String? = null

    fun connect(onMessageReceived: (AuthenticateResponse) -> Unit) {
        val request = Request.Builder()
            // .url("wss://echo.websocket.org")  // Test için public WebSocket server (WSS)
            .url(Constants.BASE_URL)  // Orijinal sunucu
            .build()

        this.listener = onMessageReceived
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
                    val response = gson.fromJson(text, AuthenticateResponse::class.java)
                    Log.d("WebSocket", "JSON parse başarılı: $response")
                    listener?.invoke(response)
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
        Log.d("WebSocket", "Gönderilen JSON: $json")
        
        if (isConnected) {
            Log.d("WebSocket", "Bağlantı aktif, mesaj gönderiliyor")
            webSocket?.send(json)
        } else {
            Log.d("WebSocket", "Bağlantı henüz kurulmadı, mesaj bekletiliyor")
            pendingMessage = json
        }
    }
}
