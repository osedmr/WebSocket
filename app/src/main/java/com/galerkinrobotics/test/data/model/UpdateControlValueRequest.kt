package com.galerkinrobotics.test.data.model

import com.google.gson.annotations.SerializedName

data class UpdateControlValueRequest(
    @SerializedName("is_request")
    val isRequest: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("params")
    val params: List<UpdateControlParam>,
    @SerializedName("method")
    val method: String
)

data class UpdateControlParam(
    @SerializedName("id")
    val id: String,
    @SerializedName("value")
    val value: Int
)