package com.buffup.sdk.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.buffup.sdk.domain.entities.Answer

class AnswerAdapter : ListAdapter<Answer, AnswerHolder>(DIFF_UTILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        return AnswerHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AnswerHolder, position: Int) {
        val answer = getItem(position)
        holder.bind(answer)
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