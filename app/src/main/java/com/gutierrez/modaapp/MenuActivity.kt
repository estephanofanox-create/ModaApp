package com.gutierrez.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardRopa.setOnClickListener {
            startActivity(Intent(this, RopaActivity::class.java))
        }
        binding.cardPedidos.setOnClickListener {
            startActivity(Intent(this, PedidosActivity::class.java))
        }
        binding.cardClientes.setOnClickListener {
            startActivity(Intent(this, ClientesActivity::class.java))
        }
        binding.cardReportes.setOnClickListener {
            startActivity(Intent(this, ReportesActivity::class.java))
        }
        binding.btnSalir.setOnClickListener {
            getSharedPreferences("sesion", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}