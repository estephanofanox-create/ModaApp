package com.gutierrez.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}