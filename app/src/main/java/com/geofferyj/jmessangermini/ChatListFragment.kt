package com.geofferyj.jmessangermini

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geofferyj.jmessangermini.databinding.FragmentChatListBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ChatListFragment : Fragment() {

    private lateinit var binding: FragmentChatListBinding
    private val args: ChatListFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = args.userId
        val dbRef = Firebase.database.reference
        val contacts = mutableListOf<Chat>()

        binding.user = userId

        dbRef.child("users").child(userId).child("chats")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {

                        snapshot.getValue<Chat>()?.let { contacts.add(it) }

                        val adapter = ChatListAdapter()

                        adapter.setOnItemClickListener { position, item ->
                            val action =
                                ChatListFragmentDirections.actionChatListFragmentToChatFragment(userId, item.receiver, item.chatId)
                            findNavController().navigate(action)
                        }
                        adapter.differ.submitList(contacts)
                        binding.rv.adapter = adapter
                    }


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


        binding.userId.editText?.doOnTextChanged { _, _, _, count ->

            binding.buttonChat.isEnabled = count > 0

        }

        binding.buttonChat.setOnClickListener {

            val receiverUserId = binding.userId.editText?.text.toString()


            dbRef.child("users").child(receiverUserId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (userId == receiverUserId) {
                        binding.userId.error = "sorry you can't chat with yourself"
                    } else if (!snapshot.exists()) {
                        binding.userId.error = "UserId does not Exist."
                    } else {

                        binding.userId.error = null
                        Log.d("ChatFF", "user exists")
                        dbRef.child("users")
                            .child(userId)
                            .child("chats")
                            .child(receiverUserId)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    if (!snapshot.exists()) {
                                        val key = dbRef.child("chats").push().key
                                        if (key != null) {
                                            dbRef.child("users")
                                                .child(userId)
                                                .child("chats")
                                                .child(receiverUserId)
                                                .setValue(Chat(key, receiverUserId))

                                            dbRef.child("users")
                                                .child(receiverUserId)
                                                .child("chats")
                                                .child(userId)
                                                .setValue(Chat(key, userId))

                                            val action =
                                                ChatListFragmentDirections.actionChatListFragmentToChatFragment(userId, receiverUserId, key)
                                            findNavController().navigate(action)
                                        }
                                    } else {
                                       val chat =  snapshot.getValue<Chat>()!!
                                        val action =
                                            ChatListFragmentDirections.actionChatListFragmentToChatFragment(userId, receiverUserId, chat.chatId)
                                        findNavController().navigate(action)

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        }

    }

    private fun writeNewPost(userId: String, username: String, title: String, body: String) {
//        // Create new post at /user-posts/$userid/$postid and at
//        // /posts/$postid simultaneously
//        val key = dbref.child("chats").push().key
//        if (key == null) {
//            Log.w(TAG, "Couldn't get push key for posts")
//            return
//        }
//
//        val post = Post(userId, username, title, body)
//        val postValues = post.toMap()
//
//        val childUpdates = hashMapOf<String, Any>(
//            "/posts/$key" to postValues,
//            "/user-posts/$userId/$key" to postValues
//        )
//
//        database.updateChildren(childUpdates)
    }

    companion object {
        private const val TAG = "ChatListFragment"
    }

}