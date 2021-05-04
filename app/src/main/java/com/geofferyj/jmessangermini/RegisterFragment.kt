package com.geofferyj.jmessangermini

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geofferyj.jmessangermini.databinding.FragmentRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val dbRef = Firebase.database.reference

        binding.loginRedirect.setOnClickListener {
            findNavController().navigate(R.id.startFragment)
        }

        binding.userId.editText?.doOnTextChanged { _, _, _, count ->

            binding.buttonProceed.isEnabled = count > 0

        }

        binding.buttonProceed.setOnClickListener {

            binding.progress.visibility = View.VISIBLE
            val userId = binding.userId.editText?.text.toString()


            dbRef.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        binding.progress.visibility = View.GONE
                        binding.userId.error = "User Name Exists please choose another"
                    } else{
                        binding.userId.error = null

                        dbRef.child("users")
                            .child(userId)
                            .setValue(userId)
                            .addOnSuccessListener {
                                Log.d(TAG, "Success")
                                binding.progress.visibility = View.GONE
                                val action = RegisterFragmentDirections.actionRegisterFragmentToChatListFragment(userId)
                                findNavController().navigate(action)
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "Failed: ${it.message}")
                                binding.progress.visibility = View.GONE
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        }
    }


    companion object {
        private const val TAG = "RegisterFragment"
    }
}