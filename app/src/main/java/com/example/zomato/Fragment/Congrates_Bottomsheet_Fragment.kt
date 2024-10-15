package com.example.zomato.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zomato.MainActivity
import com.example.zomato.R
import com.example.zomato.databinding.FragmentCongratesBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Congrates_Bottomsheet_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratesBottomsheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratesBottomsheetBinding.inflate(layoutInflater, container, false)
        gotohome()
        return binding.root
    }

    private fun gotohome() {
        binding.btnGo.setOnClickListener {
            val gohome = Intent(requireContext(),MainActivity::class.java)
            startActivity(gohome)
        }
    }

    companion object {

    }
}