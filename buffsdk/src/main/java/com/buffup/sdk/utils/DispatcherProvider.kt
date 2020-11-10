package com.buffup.sdk.utils

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Interface for providing coroutines context
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
}
