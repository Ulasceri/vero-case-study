package com.example.tasks.auth

import com.example.tasks.auth.model.AuthRequestModel
import com.example.tasks.auth.model.LoginResponse
import retrofit2.Call

class AuthService {
    private val retrofit = AuthApiClient.getClient()
    private val authApi = retrofit.create(AuthApi::class.java)

    fun getToken(): Call<LoginResponse> {
        val reqModel = AuthRequestModel("365", "1")
        return authApi.getToken(reqModel)
    }
}