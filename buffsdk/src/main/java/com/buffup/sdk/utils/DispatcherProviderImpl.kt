package com.buffup.sdk.utils

import kotlinx.coroutines.Dispatchers

class DispatcherProviderImpl : DispatcherProvider {

    override val main = Dispatchers.Main
    override val computation = Dispatchers.Default
    override val io = Dispatchers.IO
}
