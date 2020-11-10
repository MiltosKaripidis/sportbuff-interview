package com.buffup.sdk.domain.entities

import com.squareup.moshi.Json

data class Author(
    @field:Json(name = "first_name")
    val firstName: String,
    @field:Json(name = "last_name")
    val lastName: String,
    @field:Json(name = "image")
    val avatarUrl: String
)