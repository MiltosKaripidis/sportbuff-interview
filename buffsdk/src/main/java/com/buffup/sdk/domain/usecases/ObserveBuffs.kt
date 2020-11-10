package com.buffup.sdk.domain.usecases

import com.buffup.sdk.domain.entities.Answer
import com.buffup.sdk.domain.entities.Author
import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.entities.Question
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveBuffs {

    operator fun invoke(): Flow<Buff> {
        return flow {
            delay(5_000)
            emit(getBuff())
        }
    }

    private fun getBuff(): Buff {
        return Buff(
            id = 1,
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
                    id = 2,
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