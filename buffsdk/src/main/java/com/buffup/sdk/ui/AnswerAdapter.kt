package com.buffup.sdk.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.buffup.sdk.domain.entities.Answer

class AnswerAdapter(
    private val clickAnswer: (Int) -> Unit
) : ListAdapter<Answer, AnswerHolder>(DIFF_UTILS) {

    private var selectedAnswerId: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        return AnswerHolder.create(parent, clickAnswer = {
            handleClick(it)
            clickAnswer(it)
        })
    }

    private fun handleClick(answerId: Int) {
        selectedAnswerId = answerId
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        val answer = getItem(position)
        val isSelected = answer.id == selectedAnswerId
        holder.bind(answer, isSelected)
    }

    companion object {

        private val DIFF_UTILS = object : DiffUtil.ItemCallback<Answer>() {

            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
                return oldItem == newItem
            }
        }
    }
}