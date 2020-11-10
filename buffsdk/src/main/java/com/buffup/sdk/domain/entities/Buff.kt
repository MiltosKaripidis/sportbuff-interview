package com.buffup.sdk.domain.entities

data class Buff(
    val id: Int,
    val timer: Int,
    val author: Author,
    val question: Question,
    val answers: List<Answer>
)