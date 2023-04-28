package com.example.tasks.baubuddy

import com.example.tasks.baubuddy.model.TaskItemModel
import retrofit2.Call

class BauBuddyService {
    private val retrofit = BauBuddyApiClient.getClient()
    private val bauBuddyApi = retrofit.create(BauBuddyApi::class.java)

    fun getTasks(): Call<ArrayList<TaskItemModel>> {
        return bauBuddyApi.getTasks()
    }
}