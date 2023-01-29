package com.somu.dailydev

import android.app.DownloadManager.Request
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

 class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var madapter:NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)
         fetchData()
        madapter = NewsListAdapter(this)
        recyclerView.adapter = madapter

    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines/sources?apiKey=9c6768e75fad4184b0865da6f94ecdfd"
        val jsonObjectRequest = JsonObjectRequest(
            GET,
            url,
            null,

            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("category"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("urlToImage")
                    )
                    Log.d("app" , "app run successfully $url")
                    newsArray.add(news)


                }
                madapter.updateNews(newsArray)
                            },
            {
                Log.d("app" , "app crashes ")
            }

        )
                Singleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

     override fun onItemClicked(item: News) {
         val builder =  CustomTabsIntent.Builder()
         val customTabsIntent = builder.build()
         customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}