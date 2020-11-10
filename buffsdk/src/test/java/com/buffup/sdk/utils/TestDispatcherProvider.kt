package com.buffup.sdk.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDispatcherProvider : DispatcherProvider {

    override val main = TestCoroutineDispatcher()
    override val computation = TestCoroutineDispatcher()
    override val io = TestCoroutineDispatcher()
}
