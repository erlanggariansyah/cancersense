package com.dicoding.asclepius.dto.news

import com.google.gson.annotations.SerializedName

data class NewsList(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<News>
)
