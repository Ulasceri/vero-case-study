package com.example.tasks.baubuddy

import com.example.tasks.baubuddy.model.TaskItemModel
import retrofit2.Call
import retrofit2.http.*

interface BauBuddyApi {
    @GET("/dev/index.php/v1/tasks/select")
    fun getTasks(): Call<ArrayList<TaskItemModel>>
}