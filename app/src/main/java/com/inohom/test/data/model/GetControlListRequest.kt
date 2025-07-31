package com.inohom.test.data.model

import com.google.gson.annotations.SerializedName

data class GetControlListRequest(
    @SerializedName("is_request")
    val isRequest: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("params")
    val params: List<Map<String, Any>>,
    @SerializedName("method")
    val method: String
)

data class GetControlListResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("params")
    val params: List<ControlDataWrapper>,
    @SerializedName("method")
    val method: String,
    @SerializedName("error")
    val error: String?,
    @SerializedName("is_request")
    val isRequest: Boolean
)

data class ControlDataWrapper(
    @SerializedName("data")
    val data: List<Control>
)

data class Control(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type_id")
    val typeId: String,
    @SerializedName("bridge_device_id")
    val bridgeDeviceId: String,
    @SerializedName("current_value")
    val currentValue: Int,
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("temperature_settings")
    val temperatureSettings: TemperatureSettings?,
    @SerializedName("area_id")
    val areaId: String,
    @SerializedName("parameters")
    val parameters: Parameters
)

data class TemperatureSettings(
    @SerializedName("has_heating")
    val hasHeating: Boolean,
    @SerializedName("has_cooling")
    val hasCooling: Boolean,
    @SerializedName("bridge_device_id")
    val bridgeDeviceId: String,
    @SerializedName("virtual_control_id")
    val virtualControlId: String,
    @SerializedName("input_id")
    val inputId: String,
    @SerializedName("is_mode_heating")
    val isModeHeating: Boolean,
    @SerializedName("whole")
    val whole: Int,
    @SerializedName("fraction")
    val fraction: Int
)

data class Parameters(
    @SerializedName("default_value")
    val defaultValue: Int,
    @SerializedName("end_time")
    val endTime: String?,
    @SerializedName("is_notification")
    val isNotification: Boolean?,
    @SerializedName("output_number")
    val outputNumber: Int,
    @SerializedName("should_output_reverse")
    val shouldOutputReverse: Boolean,
    @SerializedName("should_remember_last_value")
    val shouldRememberLastValue: Boolean,
    @SerializedName("start_time")
    val startTime: String?
)
