package com.geofferyj.jmessangermini

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geofferyj.jmessangermini.databinding.ChatItemBinding
import com.geofferyj.jmessangermini.databinding.ContactListItemBinding

class ChatAdapter(private val userId:String) : RecyclerView.Adapter<ChatAdapter.ChatItemVH>() {

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(
            oldItem: ChatItem,
            newItem: ChatItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ChatItem,
            newItem: ChatItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((position: Int, item: ChatItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, item: ChatItem) -> Unit) {
        onItemClickListener = listener
    }


    inner class ChatItemVH(val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatItem) {

            binding.userId = userId
            binding.message = item

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(adapterPosition, item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemVH {
        val binding =
            ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatItemVH(binding)

    }

    override fun onBindViewHolder(holder: ChatItemVH, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

}