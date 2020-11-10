package com.buffup.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.asLiveData
import com.buffup.sdk.R
import com.buffup.sdk.SportBuff
import com.buffup.sdk.di.networkModule
import com.buffup.sdk.di.sportBuffModule
import com.buffup.sdk.domain.entities.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.buff_question.view.*
import kotlinx.android.synthetic.main.buff_sender.view.*
import kotlinx.android.synthetic.main.buff_view.view.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.inject

class BuffView(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet), KoinComponent {

    private lateinit var adapter: AnswerAdapter
    private val sportBuff: SportBuff by inject()
    private var vote: ((Int) -> Unit)? = null
    private var close: (() -> Unit)? = null

    init {
        startKoin {
            androidContext(context)
            modules(listOf(sportBuffModule, networkModule))
        }
        LayoutInflater.from(context).inflate(R.layout.buff_view, this)
        setupRecyclerView()
        subscribeToBuffs()
    }

    override fun onDetachedFromWindow() {
        stopKoin()
        super.onDetachedFromWindow()
    }

    private fun setupRecyclerView() {
        adapter = AnswerAdapter(clickAnswer = { vote?.invoke(it) })
        answers.adapter = adapter
    }

    private fun subscribeToBuffs() {
        sportBuff.getBuffs().asLiveData().observeForever { handleBuffResult(it) }
        sportBuff.timer.observeForever { updateTimer(it) }
        sportBuff.voted.observeForever { voted() }
        sportBuff.expired.observeForever { expired() }
        sportBuff.message.observeForever { showMessage(it) }
    }

    private fun handleBuffResult(buffResult: BuffResult) {
        vote = buffResult.vote
        close = buffResult.close
        setData(buffResult.buff)
    }

    private fun setData(buff: Buff) {
        setAuthor(buff.author)
        setQuestion(buff.question)
        setAnswers(buff.answers)
        buff_close.setOnClickListener { close?.invoke() }
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

    private fun setQuestion(question: Question) {
        question_text.text = question.title
    }

    private fun updateTimer(seconds: Int) {
        question_time.text = seconds.toString()
        question_time_progress.progress = seconds
    }

    private fun voted() {
        buff_close.isVisible = false
        question_time.isVisible = false
        question_time_progress.isVisible = false
    }

    private fun expired() {
        toggleBuffVisibility(false)
        buff_close.isVisible = true
        question_time.isVisible = true
        question_time_progress.isVisible = true
        adapter.clearSelection()
    }

    private fun showMessage(@StringRes messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT)
            .show()
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