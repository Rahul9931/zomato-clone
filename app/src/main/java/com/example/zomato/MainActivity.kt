package com.example.zomato

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zomato.Fragment.Notification_Bottom_Fragment
import com.example.zomato.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        var bnview = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnview.setupWithNavController(NavController)

        binding.bell.setOnClickListener {
            val notificationsheet = Notification_Bottom_Fragment()
            notificationsheet.show(supportFragmentManager,"notification")
        }
    }
}