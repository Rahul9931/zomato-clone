package com.example.zomato.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zomato.Adapter.CartAdapter
import com.example.zomato.Payment_Activity
import com.example.zomato.R
import com.example.zomato.databinding.FragmentCartBinding
import com.example.zomato.model.CartItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class CartFragment : Fragment() {
    // Declaration of variables
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var cartFoodname: MutableList<String>
    private lateinit var cartPrice: MutableList<String>
    private lateinit var cartImageUri: MutableList<String>
    private lateinit var cartDescription: MutableList<String>
    private lateinit var cartIngredient: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var binding: FragmentCartBinding
    private lateinit var userId:String
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
        binding = FragmentCartBinding.inflate(inflater, container, false)

        // Initialize variable
        auth = FirebaseAuth.getInstance()
        retriveCartItem()
        // Proceed button
        binding.btnProceed.setOnClickListener {
            // get order item details
            getOrderItemsDetails()
           

        }
        return binding.root
    }

    private fun getOrderItemsDetails() {
        val orderIdReference:DatabaseReference = database.reference.child("User").child(userId).child("CartItem")
        val cartFoodname = mutableListOf<String>()
        val cartPrice = mutableListOf<String>()
        val cartDescription = mutableListOf<String>()
        val cartIngredient = mutableListOf<String>()
        val cartImageUri = mutableListOf<String>()
        val cartQuantity = cartAdapter.getUpdatedItemQuantity()
        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val orderItems = foodSnapshot.getValue(CartItemModel::class.java)
                    orderItems?.foodName?.let { cartFoodname.add(it) }
                    orderItems?.foodPrice?.let { cartPrice.add(it) }
                    orderItems?.foodImage?.let { cartImageUri.add(it) }
                    orderItems?.foodDescription?.let { cartDescription.add(it) }
                    orderItems?.foodIngredient?.let { cartIngredient.add(it) }
                }
                orderNow(cartFoodname,cartPrice,cartImageUri,cartDescription,cartIngredient,cartQuantity)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to Place Order", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun orderNow(
        cartFoodname: MutableList<String>,
        cartPrice: MutableList<String>,
        cartImageUri: MutableList<String>,
        cartDescription: MutableList<String>,
        cartIngredient: MutableList<String>,
        cartQuantity: MutableList<Int>
    ) {
        if (isAdded && context !=null){
            val intent = Intent(requireContext(),Payment_Activity::class.java)
            intent.putExtra("foodName_key", cartFoodname as ArrayList<String>)
            intent.putExtra("foodPrice_key", cartPrice as ArrayList<String>)
            intent.putExtra("foodImage_key", cartImageUri as ArrayList<String>)
            intent.putExtra("foodDescription_key", cartDescription as ArrayList<String>)
            intent.putExtra("foodIngredient_key", cartIngredient as ArrayList<String>)
            intent.putExtra("foodQuantity_key", cartQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retriveCartItem() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser!!.uid
        val foodReference = database.reference.child("User").child(userId).child("CartItem")
        // List to store cart Items
        cartFoodname = mutableListOf()
        cartPrice = mutableListOf()
        cartDescription = mutableListOf()
        cartIngredient = mutableListOf()
        cartImageUri = mutableListOf()
        quantity = mutableListOf()

        // Fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val cartitems = foodSnapshot.getValue(CartItemModel::class.java)
                    cartitems?.foodName?.let { cartFoodname.add(it) }
                    cartPrice.add(cartitems!!.foodPrice!!)
                    cartitems.foodDescription?.let { cartDescription.add(it) }
                    cartitems.foodIngredient?.let { cartIngredient.add(it) }
                    cartitems.foodImage?.let { cartImageUri.add(it) }
                    cartitems.foodQuantity?.let { quantity.add(it) }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetch", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(requireContext(),cartFoodname,cartPrice,cartImageUri,cartDescription,cartIngredient,quantity)
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = cartAdapter
    }

    companion object {


    }
}