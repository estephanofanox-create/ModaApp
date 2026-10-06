package com.gutierrez.modaapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.data.ClienteDao
import com.gutierrez.modaapp.data.PedidoDao
import com.gutierrez.modaapp.databinding.ActivityPedidoBinding
import com.gutierrez.modaapp.model.Cliente

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidoBinding
    private lateinit var clienteDao: ClienteDao
    private lateinit var pedidoDao: PedidoDao

    private var clienteEncontrado: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteDao = ClienteDao(this)
        pedidoDao = PedidoDao(this)

        mostrarResumen()

        binding.btnContinuar.setOnClickListener { buscarCliente() }
        binding.btnConfirmar.setOnClickListener { confirmarPedido() }
    }

    private fun mostrarResumen() {
        val sb = StringBuilder()
        for (item in Carrito.items) {
            sb.append("• ${item.ropa.modelo} (${item.ropa.talla}) x${item.cantidad}  -  S/ %.2f\n"
                .format(item.subtotal))
        }
        binding.tvResumen.text = sb.toString().trim()
        binding.tvTotal.text = "Total: S/ %.2f".format(Carrito.total())
    }

    private fun buscarCliente() {
        val tel = binding.etTelefono.text.toString().trim()
        if (tel.length != 9) {
            toast("Ingresa un teléfono de 9 dígitos"); return
        }

        val c = clienteDao.buscarPorTelefono(tel)
        if (c != null) {
            clienteEncontrado = c
            binding.tvSaludo.text = "Hola, ${c.nombres}"
            binding.tvSaludo.visibility = View.VISIBLE
            binding.layoutNuevo.visibility = View.GONE
        } else {
            clienteEncontrado = null
            binding.tvSaludo.visibility = View.GONE
            binding.layoutNuevo.visibility = View.VISIBLE
        }
    }

    private fun confirmarPedido() {
        val tel = binding.etTelefono.text.toString().trim()
        if (tel.length != 9) {
            toast("Ingresa un teléfono de 9 dígitos"); return
        }

        val idCliente: Int
        if (clienteEncontrado != null) {
            idCliente = clienteEncontrado!!.id
        } else {
            val nombres = binding.etNombres.text.toString().trim()
            val apellidos = binding.etApellidos.text.toString().trim()
            if (nombres.isEmpty() || apellidos.isEmpty()) {
                toast("Ingresa nombres y apellidos"); return
            }
            val id = clienteDao.insertar(tel, nombres, apellidos)
            if (id <= 0) { toast("Error al registrar cliente"); return }
            idCliente = id.toInt()
        }

        if (Carrito.items.isEmpty()) { toast("Carrito vacío"); return }

        val idPedido = pedidoDao.registrar(idCliente, Carrito.items)
        if (idPedido > 0) {
            Toast.makeText(this, "Pedido #$idPedido registrado", Toast.LENGTH_LONG).show()
            Carrito.vaciar()
            finish()
        } else {
            toast("Error al registrar pedido")
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}