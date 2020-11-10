package com.buffup.sdk.domain.usecases

import androidx.test.filters.SmallTest
import com.buffup.sdk.domain.usecases.CountdownTime
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class CountdownTimeTest {

    private lateinit var countdownTime: CountdownTime

    @Before
    fun setup() {
        countdownTime = CountdownTime()
    }

    @Test
    fun `countdownTime returns expected list`() = runBlockingTest {
        val timer = 10
        val expected = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
        val result = countdownTime(timer).toList()
        assertThat(result).isEqualTo(expected)
    }
}