package com.gutierrez.modaapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gutierrez.modaapp.data.ReporteDao
import com.gutierrez.modaapp.databinding.ActivityClientesBinding

class ClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientesBinding
    private lateinit var reporteDao: ReporteDao
    private lateinit var adapter: ClienteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reporteDao = ReporteDao(this)

        adapter = ClienteAdapter(emptyList())
        binding.rvClientes.layoutManager = LinearLayoutManager(this)
        binding.rvClientes.adapter = adapter

        val lista = reporteDao.clientesConPedidos()
        adapter.actualizar(lista)

        if (lista.isEmpty()) {
            binding.rvClientes.visibility = View.GONE
            binding.tvVacio.visibility = View.VISIBLE
        } else {
            binding.rvClientes.visibility = View.VISIBLE
            binding.tvVacio.visibility = View.GONE
        }
    }
}