package com.gutierrez.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.databinding.ActivityClientesBinding

class ClientesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}