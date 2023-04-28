package com.example.tasks.baubuddy

import com.example.tasks.auth.AuthService
import okhttp3.*
import okhttp3.OkHttpClient.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BauBuddyApiClient {
    private const val BASE_URL = "https://api.baubuddy.de/"

    private fun getClientBuilder(): Builder =
        Builder().authenticator(object: Authenticator {
        override fun authenticate(route: Route?, response: Response): Request {
            if(response.code == 401) {
                val loginResponse = AuthService().getToken().execute()
                val token = loginResponse.body()?.oauth?.access_token ?: throw Exception("Access token error!!")
                return response.request.newBuilder().header("Authorization", "Bearer $token").build()
            } else {
                return response.request
            }
        }
    })

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .client(getClientBuilder().build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}