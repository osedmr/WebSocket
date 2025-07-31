package com.inohom.test.data.model

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

data class OnEntityUpdatedResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("params")
    val params: List<EntityUpdateWrapper>,
    @SerializedName("method")
    val method: String,
    @SerializedName("error")
    val error: String?,
    @SerializedName("is_request")
    val isRequest: Boolean
)

data class EntityUpdateWrapper(
    @SerializedName("entity")
    val entity: Control,
    @SerializedName("type")
    val type: String
)
