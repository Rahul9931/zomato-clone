package com.example.zomato.Fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.zomato.Adapter.PopularListAdapter
import com.example.zomato.R
import com.example.zomato.databinding.FragmentHomeBinding
import com.example.zomato.model.Menumodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItem:MutableList<Menumodel>
    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.txtViewMenu.setOnClickListener {
            val bottomSheetDialog = BottomSheetMenuFragment()
            bottomSheetDialog.show(parentFragmentManager,"botttomsheet")
        }
        retrivePopularData()
        return binding.root

    }

    private fun retrivePopularData() {
        database=FirebaseDatabase.getInstance()
        val menuRef: DatabaseReference = database.reference.child("Menu")
        menuItem= mutableListOf()
        menuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (menuSnapshot in snapshot.children){
                    val eachItem = menuSnapshot.getValue(Menumodel::class.java)
                    eachItem?.let {
                            result ->
                        menuItem.add(result)
                    }
                }
                randomPopularItem()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun randomPopularItem() {
        val index = menuItem.indices.toList().shuffled()
        val numofitemtoshow = 6
        val shuffleMenuItem = index.take(numofitemtoshow).map { menuItem[it] }

        setPopularMenuAdapter(shuffleMenuItem)
    }

    private fun setPopularMenuAdapter(shuffleMenuItem: List<Menumodel>) {
        val popularadapter = PopularListAdapter(shuffleMenuItem as MutableList<Menumodel>,requireContext())
        binding.popularrcview.layoutManager=LinearLayoutManager(requireContext())
        binding.popularrcview.adapter=popularadapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        imageSlider.setSlideAnimation(AnimationTypes.DEPTH_SLIDE)
        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                Toast.makeText(context, "Selected Image $position",Toast.LENGTH_LONG).show()
            }
        })
    }
            }