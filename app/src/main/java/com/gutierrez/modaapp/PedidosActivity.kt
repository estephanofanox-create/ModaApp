package com.gutierrez.modaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.gutierrez.modaapp.data.PedidoDao
import com.gutierrez.modaapp.databinding.ActivityPedidosBinding

class PedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosBinding
    private lateinit var pedidoDao: PedidoDao
    private lateinit var adapter: PedidoAdapter

    private var estadoActual: String = "PENDIENTE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pedidoDao = PedidoDao(this)

        adapter = PedidoAdapter(emptyList()) { id ->
            val i = Intent(this, DetallePedidoActivity::class.java)
            i.putExtra("idPedido", id)
            startActivity(i)
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                estadoActual = if (tab?.position == 0) "PENDIENTE" else "ATENDIDO"
                cargar()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        cargar()
    }

    private fun cargar() {
        val lista = pedidoDao.listarPorEstado(estadoActual)
        adapter.actualizar(lista)
        if (lista.isEmpty()) {
            binding.rvPedidos.visibility = View.GONE
            binding.tvVacio.visibility = View.VISIBLE
        } else {
            binding.rvPedidos.visibility = View.VISIBLE
            binding.tvVacio.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }
}