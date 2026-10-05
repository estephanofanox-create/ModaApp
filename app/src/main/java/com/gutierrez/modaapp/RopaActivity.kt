package com.gutierrez.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.databinding.ActivityRopaBinding

class RopaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRopaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}