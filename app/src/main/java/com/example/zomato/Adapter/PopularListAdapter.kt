package com.example.zomato.Adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zomato.Food_Details_Activity
import com.example.zomato.Start_Activity
import com.example.zomato.databinding.PopularItemRowBinding
import com.example.zomato.model.Menumodel

class PopularListAdapter(
    private val menuList: MutableList<Menumodel>,
    private val context: Context,
) : RecyclerView.Adapter<PopularListAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            PopularItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PopularViewHolder(private val binding: PopularItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                foodName.text = menuItem.foodName
                price.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodimage)
                // Click on Details
                binding.popularrow.setOnClickListener {
                    val menudetail = Intent(context, Food_Details_Activity::class.java)

                    /*val options = ActivityOptionsCompat.makeSceneTransitionAnimation(Activity(), Pair.create(foodimage,"foodimage"), Pair.create(foodName, "foodname"))
                   */ /*val options = ActivityOptions.makeSceneTransitionAnimation(Activity(),
                            utiPair.create(foodimage,"foodimage"),
                            utiPair.create(foodName, "foodname")
                        )*/
                    menudetail.putExtra("foodname", menuItem.foodName)
                    menudetail.putExtra("foodimage",menuItem.foodImage)
                    menudetail.putExtra("foodprice",menuItem.foodPrice)
                    menudetail.putExtra("fooddescription",menuItem.foodDescription)
                    menudetail.putExtra("foodingredient",menuItem.foodIngredient)
                    context.startActivity(menudetail)
                }


            }
        }

    }

}