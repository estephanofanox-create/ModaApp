package com.gutierrez.modaapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gutierrez.modaapp.databinding.ActivityCarritoBinding

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CarritoAdapter(Carrito.items) { index -> confirmarQuitar(index) }
        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter

        binding.btnHacerPedido.setOnClickListener {
            if (Carrito.items.isEmpty()) {
                Toast.makeText(this, "Tu carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, PedidoActivity::class.java))
            }
        }

        refrescar()
    }

    private fun refrescar() {
        adapter.notifyDataSetChanged()
        binding.tvTotal.text = "S/ %.2f".format(Carrito.total())

        if (Carrito.items.isEmpty()) {
            binding.rvCarrito.visibility = View.GONE
            binding.tvVacio.visibility = View.VISIBLE
            binding.btnHacerPedido.isEnabled = false
        } else {
            binding.rvCarrito.visibility = View.VISIBLE
            binding.tvVacio.visibility = View.GONE
            binding.btnHacerPedido.isEnabled = true
        }
    }

    private fun confirmarQuitar(index: Int) {
        val item = Carrito.items[index]
        AlertDialog.Builder(this)
            .setTitle("Quitar prenda")
            .setMessage("¿Quitar ${item.ropa.modelo} del carrito?")
            .setPositiveButton("Sí") { _, _ ->
                Carrito.quitar(index)
                refrescar()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        refrescar()
    }
}