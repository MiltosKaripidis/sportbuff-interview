package com.buffup.sdk.data.remote

import com.buffup.sdk.domain.entities.Buff
import com.squareup.moshi.Json

data class BuffResponse(
    @field:Json(name = "result")
    val buff: Buff
)

