package com.example.tasks

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.baubuddy.model.TaskItemModel
import com.example.tasks.baubuddy.model.TaskList


class PostAdapter: RecyclerView.Adapter<PostViewHolder>() {

    private val requestResponseItemList = mutableListOf<TaskItemModel>()
    private val filteredItemList = mutableListOf<TaskItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(filteredItemList[position])
    }

    override fun getItemCount(): Int {
        return filteredItemList.size
    }

    fun updateList(list: TaskList) {
        requestResponseItemList.clear()
        requestResponseItemList.addAll(list)
        filterList("")
    }

    fun filterList(query: String) {
        filteredItemList.clear()
        if (query.isEmpty()) {
            filteredItemList.addAll(requestResponseItemList)
        } else {
            for (item in requestResponseItemList) {
                if (item.title!!.contains(query, true) || item.task!!.contains(query, true) || item.description!!.contains(query, true)) {
                    filteredItemList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}
class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val tvTask: TextView = itemView.findViewById(R.id.tvTask)
    private val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
    private val viewColor: View = itemView.findViewById(R.id.view_color)

    fun bindView(requestResponseItem: TaskItemModel) {
        tvTitle.text = requestResponseItem.title
        tvTask.text = requestResponseItem.task
        tvDesc.text = requestResponseItem.description

        try {
            viewColor.setBackgroundColor(Color.parseColor(requestResponseItem.colorCode))
        } catch (e: Exception) {
            Log.e("PostViewHolder", "Error setting color: ${e.message}")
        }

    }

}
