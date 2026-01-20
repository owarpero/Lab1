package com.example.messengerapp.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.R
import com.example.messengerapp.data.local.MessageEntity

class MessageAdapter : ListAdapter<MessageEntity, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.message_title)
        private val bodyTextView: TextView = itemView.findViewById(R.id.message_body)

        fun bind(message: MessageEntity) {
            titleTextView.text = message.title
            bodyTextView.text = if (message.body.length > 100) {
                message.body.substring(0, 100) + "..."
            } else {
                message.body
            }
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
    override fun areItemsTheSame(old: MessageEntity, new: MessageEntity) = old.id == new.id
    override fun areContentsTheSame(old: MessageEntity, new: MessageEntity) = old == new
}
