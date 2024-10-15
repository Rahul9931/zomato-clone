package com.example.zomato.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.zomato.Adapter.BuyAgainAdapter
import com.example.zomato.R
import com.example.zomato.RecentBuy_Activity
import com.example.zomato.databinding.FragmentHistoryBinding
import com.example.zomato.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter:BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId:String
    private var listOfOrderItem:ArrayList<OrderDetails> = arrayListOf()
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
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Initialize auth
        auth = FirebaseAuth.getInstance()
        // Initialize database
        database = FirebaseDatabase.getInstance()
        // Retrieve BuyHistory
        retriveBuyHistory()
        binding.recentOrder.setOnClickListener {
            seeItemsRecentBuy()
        }

        binding.btnReceived.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("AdminUser").child("CompleteOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let {recentBuy->
            val intent = Intent(requireContext(),RecentBuy_Activity::class.java)
            intent.putExtra("RecentBuyOrderItem",listOfOrderItem)
            startActivity(intent)
        }
    }

    private fun retriveBuyHistory() {
        binding.recentOrder.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""
        val buyItemReference = database.reference.child("User").child(userId).child("BuyHistory")
        val sortQuery = buyItemReference.orderByChild("currentTime")
        sortQuery.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let { listOfOrderItem.add(it) }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()){
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsInRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("retriveBuyHistory","${error.message}")
            }

        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentOrder.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            binding.foodnameCart.text = it.foodNames?.firstOrNull()?:""
            binding.priceCart.text = it.foodPrices?.firstOrNull()?:""
            val image = it.foodImages?.firstOrNull()?:""
            val uri = Uri.parse(image)
            Glide.with(requireContext()).load(uri).into(binding.imgCart)
            listOfOrderItem.reverse()
            if (listOfOrderItem.isNotEmpty()){

            }
            val isOrderIsAccepted = listOfOrderItem[0].orderAccepted
            if (isOrderIsAccepted){
                binding.orderStatus.background.setTint(Color.GREEN)
                binding.btnReceived.visibility = View.VISIBLE
            }

        }
    }

    private fun setPreviousBuyItemsInRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()
        for(i in 1 until listOfOrderItem.size){
            listOfOrderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
        }
        val rv = binding.rvhistory
        rv.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
        rv.adapter = historyAdapter
        }
    }