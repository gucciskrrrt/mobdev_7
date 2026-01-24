package com.example.mymessenger.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.mymessenger.R
import com.example.mymessenger.data.model.Message
import com.example.mymessenger.databinding.ItemMessageBinding

class MessageAdapter(
    private val onLikeClick: (Message) -> Unit
) : ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding, onLikeClick)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MessageViewHolder(
        private val binding: ItemMessageBinding,
        private val onLikeClick: (Message) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.apply {
                textMessageName.text = message.name ?: "Неизвестно"
                textMessageEmail.text = message.email ?: "Нет email"
                textMessageBody.text = message.body ?: "Нет сообщения"
                textMessageId.text = "#${message.id}"

                imageAvatar.load(message.getAvatarUrlGenerated()) {
                    crossfade(true)
                    placeholder(R.drawable.ic_profile)
                    error(R.drawable.ic_profile)
                    transformations(CircleCropTransformation())
                }

                updateLikeButton(message.isLiked)

                buttonLike.setOnClickListener {
                    onLikeClick(message)
                }
            }
        }

        private fun updateLikeButton(isLiked: Boolean) {
            binding.buttonLike.setImageResource(
                if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            )
            binding.buttonLike.imageTintList = null
        }
    }

    private class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}
