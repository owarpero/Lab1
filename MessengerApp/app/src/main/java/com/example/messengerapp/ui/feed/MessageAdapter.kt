package com.example.messengerapp.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messengerapp.R
import com.example.messengerapp.data.local.MessageEntity
import com.google.android.material.button.MaterialButton

class MessageAdapter(
    private val onLikeClick: (MessageEntity) -> Unit
) : ListAdapter<MessageEntity, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view, onLikeClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(
        itemView: View,
        private val onLikeClick: (MessageEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatar_image)
        private val usernameTextView: TextView = itemView.findViewById(R.id.username_text)
        private val emailTextView: TextView = itemView.findViewById(R.id.email_text)
        private val titleTextView: TextView = itemView.findViewById(R.id.message_title)
        private val bodyTextView: TextView = itemView.findViewById(R.id.message_body)
        private val likeButton: MaterialButton = itemView.findViewById(R.id.like_button)

        fun bind(message: MessageEntity) {
            titleTextView.text = message.title
            bodyTextView.text = if (message.body.length > 100) {
                message.body.substring(0, 100) + "..."
            } else {
                message.body
            }

            usernameTextView.text = message.userData?.name ?: "Unknown User"
            emailTextView.text = message.userData?.email ?: ""
            emailTextView.visibility = if (message.userData?.email.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

            // Load avatar
            val avatarUrl = "https://i.pravatar.cc/150?u=${message.userId}"
            Glide.with(itemView.context)
                .load(avatarUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .circleCrop()
                .into(avatarImageView)

            // Update like button
            if (message.isLiked) {
                likeButton.setIconResource(R.drawable.ic_heart_filled)
            } else {
                likeButton.setIconResource(R.drawable.ic_heart_outline)
            }

            likeButton.setOnClickListener {
                onLikeClick(message)
            }
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
    override fun areItemsTheSame(old: MessageEntity, new: MessageEntity) = old.id == new.id
    override fun areContentsTheSame(old: MessageEntity, new: MessageEntity) = old == new
}
