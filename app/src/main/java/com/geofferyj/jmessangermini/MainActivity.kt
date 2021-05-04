package com.geofferyj.jmessangermini

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Firebase.database.setPersistenceEnabled(true)
//        val chatsRef = Firebase.database.getReference("chats")
//        chatsRef.keepSynced(true)
    }
}