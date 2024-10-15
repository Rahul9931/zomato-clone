package com.example.zomato.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zomato.databinding.CartListRowBinding
import com.example.zomato.databinding.FragmentCartBinding
import com.google.android.gms.common.util.MurmurHash3
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context:Context,
    private val cartfoodname: MutableList<String>,
    private val cartprice: MutableList<String>,
    private val cartimage: MutableList<String>,
    private val cartDescription: MutableList<String>,
    private val cartIngredient: MutableList<String>,
    private val cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    // Initialize variables
    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser!!.uid?:""
        val cartItemNumber = cartfoodname.size
        itemQuantity = IntArray(cartItemNumber){1}
        cartItemReference = database.reference.child("User").child(userId).child("CartItem")
    }
    companion object{
        private var itemQuantity:IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }
    inner class CartViewHolder(private val binding: CartListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]
                foodnameCart.text = cartfoodname[position]
                priceCart.text = cartprice[position]
                // Load image using Glide
                val uriString = cartimage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgCart)
                txtQuantity.text = quantity.toString()
                btnPlus.setOnClickListener {
                    itemQuantity[position]++
                    cartQuantity[position] = itemQuantity[position]
                    txtQuantity.text = itemQuantity[position].toString()
                }
                btnMinus.setOnClickListener {
                    if (itemQuantity[position]>1){
                        itemQuantity[position]--
                        cartQuantity[position] = itemQuantity[position]
                        txtQuantity.text = itemQuantity[position].toString()
                    }
                }
                btnDelete.setOnClickListener {
                    deleteItem(position)
                }
            }
        }
    }
    // Delete items
    private fun deleteItem(position: Int){
        val positionRetrive = position
        getUniqueKeyAtPosition(positionRetrive){uniqueKey->
            if (uniqueKey !=null){
                removeItem(position,uniqueKey)
            }
        }
    }
    private fun removeItem(position: Int, uniqueKey: String) {

        cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
            cartfoodname.removeAt(position)
            cartprice.removeAt(position)
            cartDescription.removeAt(position)
            cartimage.removeAt(position)
            cartQuantity.removeAt(position)
            // Update ItemQuantity
            itemQuantity = itemQuantity.filterIndexed{ index, i -> index!=position }.toIntArray()
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,cartfoodname.size)
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUniqueKeyAtPosition(positionRetrive: Int, onComplete:(String?)->Unit) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey:String?=null
                //loop for snapshot children
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == positionRetrive){
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartfoodname.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getUpdatedItemQuantity(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }
}