package com.buffup.sdk.domain.entities

import com.squareup.moshi.Json

data class Buff(
    val id: Int,
    @field:Json(name = "time_to_show")
    val timer: Int,
    val author: Author,
    val question: Question,
    val answers: List<Answer>
)