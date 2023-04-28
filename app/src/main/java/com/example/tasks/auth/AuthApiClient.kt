package com.example.tasks.auth


import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AuthApiClient {
    private const val BASE_URL = "https://api.baubuddy.de/"

    private fun getClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().authenticator(object: Authenticator {
        override fun authenticate(route: Route?, response: Response): Request {
            return if (response.code == 401) {
                // build retrofit client manually and call refresh token api
                val loginResponse = AuthService().getToken().execute()
                val token = loginResponse.body()?.oauth?.access_token
                    ?: throw Exception("Access token error!!")
                response.request.newBuilder().header("Authorization", "Bearer $token").build()
            } else {
                response.request
            }
        }
    })

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}