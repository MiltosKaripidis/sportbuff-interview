package com.buffup.sdk.domain.usecases

import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.repositories.BuffRepository
import kotlinx.coroutines.flow.Flow

class ObserveBuffs(
    private val buffRepository: BuffRepository
) {

    operator fun invoke(): Flow<Buff> {
        return buffRepository.getBuffs()
    }
}