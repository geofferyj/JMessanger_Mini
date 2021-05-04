package com.geofferyj.jmessangermini

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geofferyj.jmessangermini.databinding.ContactListItemBinding

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.ContactItemVH>() {

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Chat,
            newItem: Chat
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((position: Int, item: Chat) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, item: Chat) -> Unit) {
        onItemClickListener = listener
    }


    inner class ContactItemVH(val binding: ContactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Chat) {
            binding.userId.text = item.receiver
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(adapterPosition, item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemVH {
        val binding =
            ContactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactItemVH(binding)

    }

    override fun onBindViewHolder(holder: ContactItemVH, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

}