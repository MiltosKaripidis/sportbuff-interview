package com.buffup.sdk.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface BuffService {

    @GET("buffs/{buffId}")
    suspend fun getBuffs(@Path("buffId") buffId: Int): BuffResponse
}