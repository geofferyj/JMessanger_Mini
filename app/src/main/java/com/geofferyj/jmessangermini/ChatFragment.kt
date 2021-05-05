package com.geofferyj.jmessangermini

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.geofferyj.jmessangermini.databinding.FragmentChatBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatId = args.chatId
        val userId = args.from
        val receiverUserId = args.to
        val chatRef = Firebase.database.getReference("chats")
        val messages = mutableListOf<ChatItem>()

        chatRef.child(chatId).addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.getValue<ChatItem>()?.let { messages.add(it) }
                val adapter = ChatAdapter(userId)
                adapter.differ.submitList(messages)
                binding.rv.adapter = adapter
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        binding.floatingActionButton.setOnClickListener {
            val currentTime = getDateString(System.currentTimeMillis())
            val msg = binding.msgBox.editText?.text.toString()
            val chatItem = ChatItem(userId,receiverUserId,msg, currentTime)
            val msgId = chatRef.child(chatId).push().key
            if (msgId != null) {
                chatRef.child(chatId).child(msgId).setValue(chatItem)
                    .addOnSuccessListener {
                        Log.d("MIisisi", "message sent")
                    }
                    .addOnFailureListener {
                        Log.d("MIisisi", "message failed ${it.message}")
                    }
            }

            binding.msgBox.clearFocus()
            binding.msgBox.editText?.setText("")
        }
    }

    private fun getDateString(dateMillis: Long?): String {
        val pattern = "MMMM d, yyyy: h:m:s"
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val dateFormat = java.text.SimpleDateFormat(pattern, Locale.UK)
            dateFormat.format(dateMillis)
        } else {
            val dateFormat = SimpleDateFormat(pattern, Locale.UK)
            dateFormat.format(dateMillis)
        }

    }


}