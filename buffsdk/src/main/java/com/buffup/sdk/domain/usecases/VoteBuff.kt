package com.buffup.sdk.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoteBuff {

    suspend operator fun invoke(buffId: Int, answerId: Int) {
        withContext(Dispatchers.IO) {
            // TODO: Handle vote
        }
    }
}