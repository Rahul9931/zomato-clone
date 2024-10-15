package com.example.zomato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zomato.databinding.ActivityStartBinding

class Start_Activity : AppCompatActivity() {
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}