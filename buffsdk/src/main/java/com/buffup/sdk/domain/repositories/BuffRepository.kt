package com.buffup.sdk.domain.repositories

import com.buffup.sdk.domain.entities.Buff
import kotlinx.coroutines.flow.Flow

interface BuffRepository {

    fun getBuffs(): Flow<Buff>
}