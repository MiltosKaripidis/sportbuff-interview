package com.buffup.sdk.di

import com.buffup.sdk.SportBuff
import com.buffup.sdk.data.BuffRepositoryImpl
import com.buffup.sdk.data.remote.BuffService
import com.buffup.sdk.domain.repositories.BuffRepository
import com.buffup.sdk.domain.usecases.CloseBuff
import com.buffup.sdk.domain.usecases.CountdownTime
import com.buffup.sdk.domain.usecases.ObserveBuffs
import com.buffup.sdk.domain.usecases.VoteBuff
import com.buffup.sdk.utils.DispatcherProvider
import com.buffup.sdk.utils.DispatcherProviderImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val sportBuffModule = module {
    single { provideSportBuff(get(), get(), get(), get(), get()) }
    single { provideDispatcherProvider() }
    single { provideObserveBuffs(get()) }
    single { provideCountdownTime() }
    single { provideVoteBuff() }
    single { provideCloseBuff() }
    single { provideBuffRepository(get()) }
    single { provideBuffService(get()) }
}

private fun provideSportBuff(
    dispatcherProvider: DispatcherProvider,
    observeBuffs: ObserveBuffs,
    countdownTime: CountdownTime,
    voteBuff: VoteBuff,
    closeBuff: CloseBuff
): SportBuff {
    return SportBuff(dispatcherProvider, observeBuffs, countdownTime, voteBuff, closeBuff)
}

private fun provideDispatcherProvider(): DispatcherProvider {
    return DispatcherProviderImpl()
}

private fun provideObserveBuffs(buffRepository: BuffRepository): ObserveBuffs {
    return ObserveBuffs(buffRepository)
}

private fun provideCountdownTime(): CountdownTime {
    return CountdownTime()
}

private fun provideVoteBuff(): VoteBuff {
    return VoteBuff()
}

private fun provideCloseBuff(): CloseBuff {
    return CloseBuff()
}

private fun provideBuffRepository(buffService: BuffService): BuffRepository {
    return BuffRepositoryImpl(buffService)
}

private fun provideBuffService(retrofit: Retrofit): BuffService {
    return retrofit.create(BuffService::class.java)
}