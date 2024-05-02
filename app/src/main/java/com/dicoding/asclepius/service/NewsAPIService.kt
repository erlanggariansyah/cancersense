package com.dicoding.asclepius.service

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.dto.news.NewsList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {
    @GET(BuildConfig.API_NEWS_ENDPOINT_TOP_HEADLINES)
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("q") q: String,
        @Query("country") country: String,
        @Query("category") category: String
    ): Call<NewsList>
}
