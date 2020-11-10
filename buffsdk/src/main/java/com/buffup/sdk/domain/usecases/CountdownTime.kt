package com.buffup.sdk.domain.usecases

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountdownTime {

    operator fun invoke(timer: Int): Flow<Int> {
        return flow {
            // Adds 1 because it's zero-based.
            repeat(timer + 1) {
                val countdown = timer - it
                emit(countdown)
                delay(1_000)
            }
        }
    }
}