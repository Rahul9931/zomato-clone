package com.example.zomato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.zomato.Fragment.Congrates_Bottomsheet_Fragment
import com.example.zomato.databinding.ActivityPaymentBinding
import com.example.zomato.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Payment_Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cartFoodname: MutableList<String>
    private lateinit var cartPrice: MutableList<String>
    private lateinit var cartImageUri: MutableList<String>
    private lateinit var cartDescription: MutableList<String>
    private lateinit var cartIngredient: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String


    private lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()

        // Setting user data
        setUserData()

        val intent = intent
        cartFoodname = intent.getStringArrayListExtra("foodName_key") as ArrayList<String>
        cartPrice = intent.getStringArrayListExtra("foodPrice_key") as ArrayList<String>
        cartImageUri = intent.getStringArrayListExtra("foodImage_key") as ArrayList<String>
        cartDescription = intent.getStringArrayListExtra("foodDescription_key") as ArrayList<String>
        cartIngredient = intent.getStringArrayListExtra("foodIngredient_key") as ArrayList<String>
        quantity = intent.getIntegerArrayListExtra("foodQuantity_key") as ArrayList<Int>

        totalAmount = calculateTotalAmount().toString()
        // Setting Total amount
        binding.txtTotalAmount.setText(totalAmount)
        // Button place order
        binding.btnPlaceOrder.setOnClickListener {
            //getting data from EditText
            name = binding.edtName.text.toString().trim()
            address = binding.edtAddress.text.toString().trim()
            phone = binding.edtPhone.text.toString().trim()
            //check user info exist or not
            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please Fill all details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
        // Button Dismiss
        binding.btndismiss.setOnClickListener {
            finish()
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("AdminUser").child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId,
            name,
            cartFoodname,
            cartImageUri,
            cartPrice,
            quantity,
            address,
            totalAmount,
            phone,
            false,
            false,
            itemPushKey,
            time
        )
        val orderRef = databaseReference.child("AdminUser").child("OrderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val CongrateBottomsheet = Congrates_Bottomsheet_Fragment()
            CongrateBottomsheet.show(supportFragmentManager, "Place Order")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Place Order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("User").child(userId).child("BuyHistory").child(orderDetails.itemPushKey!!).setValue(orderDetails)
            .addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("User").child(userId).child("CartItem")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until cartPrice.size) {
            val price = cartPrice[i].toInt()
            val quantity = quantity[i]
            totalAmount += price * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("User").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val address = snapshot.child("address").getValue(String::class.java)
                        val phone = snapshot.child("phone").getValue(String::class.java)
                        binding.edtName.setText(name)
                        binding.edtAddress.setText(address)
                        binding.edtPhone.setText(phone)
                    } else {
                        Toast.makeText(this@Payment_Activity, "data not exist", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Payment_Activity, "Request Cancel", Toast.LENGTH_SHORT)
                        .show()
                }

            })
        }
    }
}