package com.dicoding.asclepius.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.configuration.NewsAPIConfiguration
import com.dicoding.asclepius.dto.news.NewsList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.newsProgessBar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerNews)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val newsClient = NewsAPIConfiguration.getNewsAPIService().getTopHeadlines(BuildConfig.API_NEWS_KEY, "cancer", "us", "health")
            newsClient.enqueue(object: Callback<NewsList> {
                override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.adapter = NewsAdapter(response.body()?.articles!!)
                }

                override fun onFailure(call: Call<NewsList>, t: Throwable) {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, "Error fetch News API", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }
}
