package com.buffup.sdk.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.buffup.sdk.R
import com.buffup.sdk.domain.entities.Answer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.buff_answer.*

class AnswerHolder(
    override val containerView: View,
    private val clickAnswer: (Int) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(answer: Answer, isSelected: Boolean) {
        val context = itemView.context

        answer_text.text = answer.title
        val colorId = if (isSelected) R.color.test_color_light else R.color.test_color_dark

        answer_text.setTextColor(ContextCompat.getColor(context, colorId))
        val drawableId = if (isSelected) R.drawable.selected_answer else R.drawable.light_bg

        itemView.background = ContextCompat.getDrawable(context, drawableId)
        itemView.setOnClickListener { clickAnswer(answer.id) }
    }

    companion object {

        fun create(
            viewGroup: ViewGroup,
            clickAnswer: (Int) -> Unit
        ): AnswerHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.buff_answer, viewGroup, false)
            return AnswerHolder(view, clickAnswer)
        }
    }
}