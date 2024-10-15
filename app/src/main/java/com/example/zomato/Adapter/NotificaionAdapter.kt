package com.example.zomato.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zomato.databinding.NotificationItemRowBinding

class NotificaionAdapter(
    private var notification_image: List<Int>,
    private var notification_text: List<String>
) : RecyclerView.Adapter<NotificaionAdapter.NotifictionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifictionViewHolder {
        var binding =
            NotificationItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifictionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notification_image.size
    }

    override fun onBindViewHolder(holder: NotifictionViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class NotifictionViewHolder(private var binding: NotificationItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.imgNotification.setImageResource(notification_image[position])
            binding.txtNotification.text = notification_text[position]
        }

    }
}