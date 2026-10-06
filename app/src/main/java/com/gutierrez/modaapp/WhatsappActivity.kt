package com.gutierrez.modaapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.data.PedidoDao
import com.gutierrez.modaapp.databinding.ActivityWhatsappBinding

class WhatsappActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatsappBinding
    private lateinit var pedidoDao: PedidoDao

    private var idPedido: Int = 0
    private var telefonoCliente: String = ""
    private var mensajeCliente: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pedidoDao = PedidoDao(this)

        idPedido = intent.getIntExtra("idPedido", 0)
        telefonoCliente = intent.getStringExtra("telefono") ?: ""

        mensajeCliente = pedidoDao.obtenerMensajePedido(idPedido, telefonoCliente)
        binding.tvMensaje.text = mensajeCliente

        binding.btnCliente.setOnClickListener {
            abrirWhatsApp(telefonoCliente, mensajeCliente)
        }

        binding.btnTienda.setOnClickListener {
            val telAdmin = pedidoDao.obtenerTelefonoAdmin()
            if (telAdmin.isEmpty()) {
                toast("No hay teléfono del administrador")
                return@setOnClickListener
            }
            val mensajeAdmin = mensajeCliente
            abrirWhatsApp(telAdmin, mensajeAdmin)
        }

        binding.btnListo.setOnClickListener { finish() }
    }

    private fun abrirWhatsApp(telefono: String, mensaje: String) {
        val url = "https://wa.me/51$telefono?text=" + Uri.encode(mensaje)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            toast("WhatsApp no está instalado")
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}