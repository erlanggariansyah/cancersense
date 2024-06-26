package com.dicoding.asclepius.dto.news

import com.google.gson.annotations.SerializedName

data class Source(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String
)
