package com.example.zomato.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zomato.Adapter.MenuAdapter
import com.example.zomato.databinding.FragmentBottomSheetMenuBinding
import com.example.zomato.model.Menumodel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BottomSheetMenuFragment : BottomSheetDialogFragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItem:MutableList<Menumodel>
    private lateinit var binding: FragmentBottomSheetMenuBinding
    private lateinit var databaseReference:DatabaseReference
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
       binding = FragmentBottomSheetMenuBinding.inflate(inflater, container,false)

        binding.btndismiss.setOnClickListener {
            dismiss()
        }
        retriveData()
        return binding.root
    }

    private fun retriveData() {
        database=FirebaseDatabase.getInstance()
        val menuRef:DatabaseReference = database.reference.child("Menu")
        menuItem= mutableListOf()
        menuRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (menuSnapshot in snapshot.children){
                    val eachItem = menuSnapshot.getValue(Menumodel::class.java)
                    eachItem?.let {
                            result ->
                        menuItem.add(result)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter() {
        if (menuItem.isNotEmpty()){
            binding.rvMenu.layoutManager=LinearLayoutManager(requireContext())
            val menuadapter = MenuAdapter(menuItem,requireContext())
            binding.rvMenu.adapter = menuadapter
        }
        else{
            Toast.makeText(requireContext(), "Adapter Not Set", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {

    }
}