package com.example.tasks.auth.model


import com.google.gson.annotations.SerializedName

data class Oauth(
    @SerializedName("access_token")
    val access_token: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("scope")
    val scope: Any?,
    @SerializedName("token_type")
    val tokenType: String?
)