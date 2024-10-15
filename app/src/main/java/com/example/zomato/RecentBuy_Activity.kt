package com.example.zomato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zomato.Adapter.RecentBuyAdapter
import com.example.zomato.databinding.ActivityRecentBuyBinding
import com.example.zomato.model.OrderDetails
import kotlin.math.log

class RecentBuy_Activity : AppCompatActivity() {
    private val binding: ActivityRecentBuyBinding by lazy {
        ActivityRecentBuyBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBackDetails.setOnClickListener {
            finish()
        }
        val recentOrderItems =
            intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodNames as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrices as ArrayList<String>
                allFoodImages = recentOrderItem.foodImages as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recyclerviewRecent
        rv.layoutManager = LinearLayoutManager(this)
        val RecentItemAdapter = RecentBuyAdapter(this, allFoodNames,allFoodPrices,allFoodImages,allFoodQuantities)
        rv.adapter = RecentItemAdapter
    }
}