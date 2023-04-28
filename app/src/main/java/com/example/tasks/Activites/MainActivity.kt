package com.example.tasks.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tasks.auth.AuthService
import com.example.tasks.baubuddy.BauBuddyService
import com.example.tasks.baubuddy.model.TaskList
import com.google.gson.Gson
import okhttp3.*
import AppPreferences
import AppPreferences.taskList
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView



import com.example.tasks.PostAdapter
import com.example.tasks.R

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var postAdapter: PostAdapter
    private lateinit var client: OkHttpClient
    private lateinit var authService: AuthService
    private lateinit var bauBuddyService: BauBuddyService

    override fun onCreate(savedInstanceState: Bundle?) {
        AppPreferences.setup(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.myRecyclerView)
        postAdapter = PostAdapter()
        recyclerView.adapter = postAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        client = OkHttpClient()
        authService = AuthService()
        bauBuddyService = BauBuddyService()
        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }
        fetchData()
    }
    private fun showToastEvery10Seconds() {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                getTaskList()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Data updated", Toast.LENGTH_SHORT).show()
                }
                handler.postDelayed(this, 3600000) // 10 saniye sonra tekrar çağırmak için
            }
        }
        handler.post(runnable)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.nav_menu, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                postAdapter.filterList(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                postAdapter.filterList(newText.orEmpty())
                return false
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_QR -> {
                val intent = Intent(this, QRCodeScanner::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_search -> {
                val searchView = item.actionView as SearchView
                searchView.queryHint = "Type here to search"

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getTaskList() {
        val response = bauBuddyService.getTasks().execute()
        taskList = Gson().toJson(response.body())
    }
    private fun fetchData() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        getTaskList()
        showToastEvery10Seconds()
        runOnUiThread {
            if (taskList != null) {
                val taskListFromJson = Gson().fromJson(taskList, TaskList::class.java)
                postAdapter.updateList(taskListFromJson)
            }
            swipeRefreshLayout.isRefreshing = false
            if (swipeRefreshLayout.isRefreshing) {
                fetchData()
            }
        }
    }
}


