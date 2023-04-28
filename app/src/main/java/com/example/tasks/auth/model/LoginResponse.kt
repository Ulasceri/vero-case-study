package com.example.tasks.auth.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("apiVersion")
    val apiVersion: String?,
    @SerializedName("oauth")
    val oauth: Oauth?,
    @SerializedName("permissions")
    val permissions: List<String?>?,
    @SerializedName("showPasswordPrompt")
    val showPasswordPrompt: Boolean?,

)