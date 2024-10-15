package com.example.zomato.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zomato.Adapter.NotificaionAdapter
import com.example.zomato.R
import com.example.zomato.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Fragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentNotificationBottomBinding
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
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater, container, false)
        SetNotificationRecyclerView()
        return binding.root
    }

    private fun SetNotificationRecyclerView() {
        val N_txt = listOf<String>(
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver",
            "Congrats Your Order Placed"
        )
        val N_img = listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)
        val notificationRecycler = NotificaionAdapter(N_img,N_txt)
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
       binding.rvNotification.adapter = notificationRecycler
    }

    companion object {

    }
}