package com.galerkinrobotics.test.data.model

data class AuthenticateRequest(
    val is_request: Boolean = true,
    val id: Int ,
    val params: List<AuthParam>,
    val method: String
)

data class AuthParam(
    val username: String,
    val password: String
)


//dönen cevap modeli
data class AuthenticateResponse(
    val is_request: Boolean,
    val id: Int,
    val params: List<String>,  // Kullanıcı adı dönecek: ["demo"]
    val method: String,        // "OnAuthenticated"
    val error: Any?            // null ise hata yok
)