package com.example.zomato.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zomato.Adapter.MenuAdapter
import com.example.zomato.databinding.FragmentSearchBinding
import com.example.zomato.model.Menumodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val menuItems = mutableListOf<Menumodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        retriveMenuItems()
        setUpSearchView()
        return binding.root
    }

    private fun retriveMenuItems() {
        // Get Database Reference
        database = FirebaseDatabase.getInstance()
        // Reference to the Menu Node
        val MenuReference = database.reference.child("Menu")
        MenuReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuFood = foodSnapshot.getValue(Menumodel::class.java)
                    menuFood?.let { menuItems.add(it) }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(menuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<Menumodel>) {
        binding.rvsearch.layoutManager = LinearLayoutManager(requireContext())
        searchAdapter = MenuAdapter(filteredMenuItem,requireContext())
        binding.rvsearch.adapter = searchAdapter
    }

    private fun setUpSearchView(){
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filtermenuItem(query)
                return true
            }

            override fun onQueryTextChange(newQuery: String): Boolean {
                filtermenuItem(newQuery)
                return true
            }

        })
    }

    private fun filtermenuItem(query: String) {
        val filteringMenuItem = menuItems.filter {
            it.foodName?.contains(query,ignoreCase = true)==true
        }
        setAdapter(filteringMenuItem)
    }
}