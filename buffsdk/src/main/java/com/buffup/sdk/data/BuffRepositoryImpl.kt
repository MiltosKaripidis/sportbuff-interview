package com.buffup.sdk.data

import com.buffup.sdk.data.remote.BuffService
import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.repositories.BuffRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BuffRepositoryImpl(
    private val buffService: BuffService
) : BuffRepository {

    override fun getBuffs(): Flow<Buff> {
        return flow {
            for (it in 1..5) {
                delay(30_000)
                val response = buffService.getBuffs(it)
                emit(response.buff)
            }
        }
    }
}