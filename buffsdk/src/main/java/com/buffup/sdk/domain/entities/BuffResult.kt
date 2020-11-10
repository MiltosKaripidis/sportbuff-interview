package com.buffup.sdk.domain.entities

data class BuffResult(
    val buff: Buff,
    val close: () -> Unit,
    val vote: (Int) -> Unit
)