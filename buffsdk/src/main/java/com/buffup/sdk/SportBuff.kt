package com.buffup.sdk

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.entities.BuffResult
import com.buffup.sdk.domain.entities.VotedException
import com.buffup.sdk.domain.usecases.CloseBuff
import com.buffup.sdk.domain.usecases.CountdownTime
import com.buffup.sdk.domain.usecases.ObserveBuffs
import com.buffup.sdk.domain.usecases.VoteBuff
import com.buffup.sdk.utils.DispatcherProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SportBuff(
    private val dispatcherProvider: DispatcherProvider,
    private val observeBuffs: ObserveBuffs,
    private val countdownTime: CountdownTime,
    private val voteBuff: VoteBuff,
    private val closeBuff: CloseBuff
) {
    private val _timer: MutableLiveData<Int> = MutableLiveData()
    val timer: LiveData<Int> = _timer
    private val _voted: MutableLiveData<Unit> = MutableLiveData()
    val voted: LiveData<Unit> = _voted
    private val _expired: MutableLiveData<Unit> = MutableLiveData()
    val expired: LiveData<Unit> = _expired
    private val _message: MutableLiveData<Int> = MutableLiveData()
    val message: LiveData<Int> = _message

    private var selectedAnswerId: Int = -1

    @VisibleForTesting
    var timerJob: Job? = null

    fun getBuffs(): Flow<BuffResult> {
        return observeBuffs()
            .map { handleBuff(it) }
            .catch { _message.value = R.string.error_get_buffs }
    }

    private fun handleBuff(buff: Buff): BuffResult {
        startTimerJob(buff)
        return BuffResult(
            buff = buff,
            vote = { answerId ->
                selectedAnswerId = answerId
                if (!timerJob?.isCancelled!!) timerJob?.cancel(VotedException())
            },
            close = {
                if (!timerJob?.isCancelled!!) timerJob?.cancel()
                GlobalScope.launch(dispatcherProvider.main) {
                    closeBuff(buff.id)
                }
            }
        )
    }

    private fun startTimerJob(buff: Buff) {
        timerJob = GlobalScope.launch(dispatcherProvider.main) {
            countdownTime(buff.timer)
                .collect { _timer.value = it }
        }
        timerJob?.invokeOnCompletion {
            GlobalScope.launch(dispatcherProvider.main) {
                // When job is canceled, meaning user has voted.
                if (it is VotedException) {
                    _voted.value = Unit
                    delay(2_000)
                }

                _expired.value = Unit
                if (selectedAnswerId != -1) voteBuff(buff.id, selectedAnswerId)
            }
        }
    }
}