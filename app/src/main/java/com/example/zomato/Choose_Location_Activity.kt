package com.example.zomato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.zomato.databinding.ActivityChooseLocationBinding

class Choose_Location_Activity : AppCompatActivity() {
    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val locationlist = arrayOf("Ghaziabad","Gautam Buddha Nagar","Faridabad","Jaipur")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, locationlist)
        val autoCompleteTextView=binding.locationList
        autoCompleteTextView.setAdapter(adapter)
    }
}