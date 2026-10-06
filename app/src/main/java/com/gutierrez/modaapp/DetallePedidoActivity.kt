package com.gutierrez.modaapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gutierrez.modaapp.data.PedidoDao
import com.gutierrez.modaapp.databinding.ActivityDetallePedidoBinding

class DetallePedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePedidoBinding
    private lateinit var pedidoDao: PedidoDao
    private var idPedido: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pedidoDao = PedidoDao(this)
        idPedido = intent.getIntExtra("idPedido", 0)

        binding.tvTitulo.text = "Pedido #$idPedido"

        val datos = pedidoDao.datosPedido(idPedido)
        if (datos != null) {
            binding.tvCliente.text = "Cliente: ${datos.first}"
            binding.tvTelefono.text = "Teléfono: ${datos.second}"
            binding.tvFecha.text = "Fecha: ${datos.third}"
        }

        val detalle = pedidoDao.listarDetalle(idPedido)
        binding.rvDetalle.layoutManager = LinearLayoutManager(this)
        binding.rvDetalle.adapter = DetallePedidoAdapter(detalle)

        var total = 0.0
        for (d in detalle) total += d.precioUnit * d.cantidad
        binding.tvTotal.text = "Total: S/ %.2f".format(total)

        binding.btnAtender.setOnClickListener { confirmarAtender() }
    }

    private fun confirmarAtender() {
        AlertDialog.Builder(this)
            .setTitle("Marcar como atendido")
            .setMessage("Se descontará el stock de cada prenda. ¿Continuar?")
            .setPositiveButton("Sí") { _, _ ->
                val error = pedidoDao.atender(idPedido)
                if (error == null) {
                    Toast.makeText(this, "Pedido atendido", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}