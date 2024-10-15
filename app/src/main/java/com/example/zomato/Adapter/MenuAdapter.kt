package com.example.zomato.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zomato.Food_Details_Activity
import com.example.zomato.databinding.PopularItemRowBinding
import com.example.zomato.model.Menumodel

class MenuAdapter(
    private val menuList: List<Menumodel>,
    private val context: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding =
            PopularItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: PopularItemRowBinding) :
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