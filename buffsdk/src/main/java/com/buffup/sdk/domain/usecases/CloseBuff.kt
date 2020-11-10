package com.buffup.sdk.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CloseBuff {

    suspend operator fun invoke(buffId: Int) {
        withContext(Dispatchers.IO) {
            // TODO: Handle close
        }
    }
}