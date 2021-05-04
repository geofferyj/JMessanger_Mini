package com.geofferyj.jmessangermini

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geofferyj.jmessangermini.databinding.FragmentStartBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbRef = Firebase.database.reference


        binding.loginRedirect.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.userId.editText?.doOnTextChanged { _, _, _, count ->

            binding.buttonProceed.isEnabled = count > 0

        }

        binding.buttonProceed.setOnClickListener {

            binding.progress.visibility = View.VISIBLE
            val userId = binding.userId.editText?.text.toString()


            dbRef.child("users").child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        binding.progress.visibility = View.GONE
                        binding.userId.error = "UserId does not Exist."
                    } else {
                        binding.userId.error = null
                        binding.progress.visibility = View.GONE
                        val action = StartFragmentDirections.actionStartFragmentToChatListFragment(userId)
                        findNavController().navigate(action)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        }

    }

    companion object {
        private const val TAG = "StartFragment"
    }

}