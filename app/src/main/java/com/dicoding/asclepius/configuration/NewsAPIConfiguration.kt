package com.dicoding.asclepius.configuration

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.service.NewsAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsAPIConfiguration {
    fun getNewsAPIService(): NewsAPIService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(NewsAPIService::class.java)
    }
}
