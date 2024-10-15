package com.example.zomato

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.zomato.databinding.ActivityFoodDetailsBinding
import com.example.zomato.model.CartItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Food_Details_Activity : AppCompatActivity() {
    private var foodname:String?=null
    private var foodprice:String?=null
    private var foodimage:String?=null
    private var foodingredient:String?=null
    private var fooddescription:String?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityFoodDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // auth initialize
        auth = FirebaseAuth.getInstance()

        foodname = intent.getStringExtra("foodname")
        foodprice = intent.getStringExtra("foodprice")
        fooddescription = intent.getStringExtra("fooddescription")
        foodingredient = intent.getStringExtra("foodingredient")
        foodimage = intent.getStringExtra("foodimage")
        binding.txtFoodnameDetails.text = foodname
        binding.descriptionDetails.text = fooddescription
        binding.ingredientDetails.text = foodingredient
        Glide.with(applicationContext).load(Uri.parse(foodimage)).into(binding.foodimageDetails)
        binding.btnBackDetails.setOnClickListener {
            finish()
        }

        binding.btnAddcartDetails.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        // Initialize variables
        val database=FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        val cartData = CartItemModel(foodname.toString(), foodprice.toString(),fooddescription.toString(),foodimage.toString(),1)

        // save data
        database.child("User").child(userId).child("CartItem").push().setValue(cartData)
            .addOnSuccessListener {
                Toast.makeText(this, "Items added into cart successful", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Items not added into cart", Toast.LENGTH_SHORT).show()
            }
    }

}