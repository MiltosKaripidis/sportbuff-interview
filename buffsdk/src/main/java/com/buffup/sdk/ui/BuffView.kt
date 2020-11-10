package com.buffup.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.buffup.sdk.R
import com.buffup.sdk.domain.entities.Answer
import com.buffup.sdk.domain.entities.Author
import com.buffup.sdk.domain.entities.Buff
import com.buffup.sdk.domain.entities.Question
import com.buffup.sdk.domain.usecases.CloseBuff
import com.buffup.sdk.domain.usecases.CountdownTime
import com.buffup.sdk.domain.usecases.ObserveBuffs
import com.buffup.sdk.domain.usecases.VoteBuff
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.buff_question.view.*
import kotlinx.android.synthetic.main.buff_sender.view.*
import kotlinx.android.synthetic.main.buff_view.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class BuffView(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private val observeBuffs = ObserveBuffs()
    private val countdownTime = CountdownTime()
    private val voteBuff = VoteBuff()
    private val closeBuff = CloseBuff()
    private lateinit var adapter: AnswerAdapter
    private var activeBuffId: Int = -1
    private var selectedAnswerId: Int = -1
    private var timerJob: Job? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.buff_view, this)
        setupRecyclerView()
        subscribeToBuffs()
    }

    private fun subscribeToBuffs() {
        GlobalScope.launch(Dispatchers.Main) {
            observeBuffs()
                .collect { setData(it) }
        }
    }

    private fun setupRecyclerView() {
        adapter = AnswerAdapter(clickAnswer = {
            selectedAnswerId = it
            if (!timerJob?.isCancelled!!) timerJob?.cancel() // TODO: Remove nullable
        })
        answers.adapter = adapter
    }

    private fun setData(buff: Buff) {
        activeBuffId = buff.id
        setAuthor(buff.author)
        setQuestion(buff.question, buff.timer)
        setAnswers(buff.answers)
        buff_close.setOnClickListener {
            toggleBuffVisibility(false)
            closeBuff(activeBuffId)
        }
        toggleBuffVisibility(true)
    }

    private fun toggleBuffVisibility(enabled: Boolean) {
        sender.isVisible = enabled
        question.isVisible = enabled
        answers.isVisible = enabled
    }

    private fun setAuthor(author: Author) {
        Glide.with(context)
            .load(author.avatarUrl)
            .into(sender_image)
        sender_name.text =
            context.getString(R.string.author_name, author.firstName, author.lastName)
    }

    private fun setQuestion(question: Question, timer: Int) {
        question_text.text = question.title

        timerJob = GlobalScope.launch(Dispatchers.Main) {
            countdownTime(timer).collect {
                question_time.text = it.toString()
                question_time_progress.progress = it
            }
        }
        timerJob?.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                // When job is canceled, meaning user has voted.
                if (it != null) {
                    buff_close.isVisible = false
                    question_time.isVisible = false
                    question_time_progress.isVisible = false
                    delay(2_000)
                }

                toggleBuffVisibility(false)
                if (selectedAnswerId != -1) voteBuff(activeBuffId, selectedAnswerId)
            }
        }
    }

    private fun setAnswers(answers: List<Answer>) {
        adapter.submitList(answers)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is VideoView) {
            video_content.addView(child, params)
        } else {
            super.addView(child, index, params)
        }
    }
}