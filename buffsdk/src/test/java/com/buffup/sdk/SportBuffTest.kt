package com.buffup.sdk

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.buffup.sdk.domain.entities.Answer
import com.buffup.sdk.domain.entities.Author
import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.entities.Question
import com.buffup.sdk.domain.usecases.CloseBuff
import com.buffup.sdk.domain.usecases.CountdownTime
import com.buffup.sdk.domain.usecases.ObserveBuffs
import com.buffup.sdk.domain.usecases.VoteBuff
import com.buffup.sdk.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class SportBuffTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sportBuff: SportBuff

    private val dispatcherProvider = TestDispatcherProvider()

    @MockK
    private lateinit var observeBuffs: ObserveBuffs

    private val countdownTime = CountdownTime()

    @MockK
    private lateinit var voteBuff: VoteBuff

    @MockK
    private lateinit var closeBuff: CloseBuff

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        sportBuff = SportBuff(
            dispatcherProvider,
            observeBuffs,
            countdownTime,
            voteBuff,
            closeBuff
        )
    }

    @Test
    fun `trigger expired when timer stops`() =
        dispatcherProvider.main.runBlockingTest {
            every { observeBuffs() } returns flow { emit(getBuff()) }

            sportBuff.getBuffs().toList()

            sportBuff.timerJob?.join()
            assertThat(sportBuff.expired.value).isEqualTo(Unit)
        }

    @Test
    fun `trigger voted when vote is invoked`() =
        dispatcherProvider.main.runBlockingTest {
            every { observeBuffs() } returns flow { emit(getBuff()) }
            coEvery { voteBuff(any(), any()) } returns Unit

            val bufferResults = sportBuff.getBuffs().toList()
            bufferResults[0].vote(ANSWER_ID)

            assertThat(sportBuff.voted.value).isEqualTo(Unit)
        }

    @Test
    fun `verify closeBuff when close is invoked`() =
        dispatcherProvider.main.runBlockingTest {
            every { observeBuffs() } returns flow { emit(getBuff()) }
            coEvery { closeBuff(any()) } returns Unit

            val bufferResults = sportBuff.getBuffs().toList()
            bufferResults[0].close()

            assertThat(sportBuff.expired.value).isEqualTo(Unit)
            coVerify { closeBuff(BUFF_ID) }
        }

    companion object {

        private const val BUFF_ID = 1
        private const val ANSWER_ID = 2

        private fun getBuff(): Buff {
            return Buff(
                id = BUFF_ID,
                timer = 10,
                author = Author(
                    firstName = "Ronaldo",
                    lastName = "",
                    avatarUrl = "https://res.cloudinary.com/dtgno0lg2/image/upload/v1582030534/avatars/Ronaldo_2x_yw2pnz.png"
                ),
                question = Question(
                    id = 1,
                    title = "Kaio Jorge has 4 goals this tournament – I think he will score again today. What do you think?"
                ),
                answers = listOf(
                    Answer(
                        id = 1,
                        title = "No goals!"
                    ),
                    Answer(
                        id = ANSWER_ID,
                        title = "One goal!"
                    ),
                    Answer(
                        id = 3,
                        title = "Two or more!"
                    )
                )
            )
        }
    }
}