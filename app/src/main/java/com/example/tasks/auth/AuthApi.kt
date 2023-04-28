package com.example.tasks.auth

import com.example.tasks.auth.model.AuthRequestModel
import com.example.tasks.auth.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface AuthApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Basic QVBJX0V4cGxvcmVyOjEyMzQ1NmlzQUxhbWVQYXNz")
    @POST("/index.php/login")
    fun getToken(@Body authRequestModel: AuthRequestModel): Call<LoginResponse>
}