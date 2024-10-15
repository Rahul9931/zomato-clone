package com.example.zomato.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zomato.R
import com.example.zomato.databinding.FragmentProfileBinding
import com.example.zomato.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Set user Data
        setUserData()
        // Save user Data
        binding.btnSaveProfile.setOnClickListener {
            val name = binding.nameProfile.text.toString()
            val address = binding.addressProfile.text.toString()
            val email = binding.emailProfile.text.toString()
            val phone = binding.phoneProfile.text.toString()

            updateUserData(name,address,email,phone)
        }
        binding.apply {
            nameProfile.isEnabled = false
            addressProfile.isEnabled = false
            emailProfile.isEnabled = false
            phoneProfile.isEnabled = false
        }
        binding.btnEdit.setOnClickListener {
            binding.apply {
                nameProfile.isEnabled = !nameProfile.isEnabled
                addressProfile.isEnabled = !addressProfile.isEnabled
                emailProfile.isEnabled = !emailProfile.isEnabled
                phoneProfile.isEnabled = !phoneProfile.isEnabled
            }
        }
        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId!=null){
            val userReference = database.getReference("User").child(userId)
            userReference.child("name").setValue(name).addOnFailureListener {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
            userReference.child("email").setValue(email).addOnFailureListener {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
            userReference.child("address").setValue(address).addOnFailureListener {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
            userReference.child("phone").setValue(phone).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Successfully Updated", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId!=null){
            val userRef = database.getReference("User").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile!=null){
                            binding.nameProfile.setText(userProfile.name)
                            binding.addressProfile.setText(userProfile.address)
                            binding.emailProfile.setText(userProfile.email)
                            binding.phoneProfile.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}