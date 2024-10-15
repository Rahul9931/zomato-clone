package com.example.zomato.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zomato.databinding.HistoryItemRowBinding

class BuyAgainAdapter(
    private val history_foodname: List<String>,
    private val history_price: List<String>,
    private val history_image: List<String>,
    private var context:Context
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = HistoryItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return history_foodname.size
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class BuyAgainViewHolder(private val binding: HistoryItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodnameHistory.text = history_foodname[position]
                priceHistory.text = history_price[position]
                val uriString = history_image[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(binding.imgHistory)
            }
        }

    }
}