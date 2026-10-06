package com.gutierrez.modaapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.gutierrez.modaapp.data.CategoriaDao
import com.gutierrez.modaapp.data.RopaDao
import com.gutierrez.modaapp.databinding.ActivityCatalogoBinding
import com.gutierrez.modaapp.model.Categoria
import com.gutierrez.modaapp.model.Ropa

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var categoriaDao: CategoriaDao
    private lateinit var ropaDao: RopaDao
    private lateinit var adapter: CatalogoAdapter
    private var categorias: List<Categoria> = emptyList()
    private var idCategoriaActual: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoriaDao = CategoriaDao(this)
        ropaDao = RopaDao(this)

        adapter = CatalogoAdapter(emptyList()) { ropa -> preguntarCantidad(ropa) }
        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter

        binding.tvCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        categorias = categoriaDao.listar()
        armarChips()
        cargarRopa(null)
    }

    private fun armarChips() {
        val chipTodas = Chip(this).apply {
            text = "Todas"
            isCheckable = true
            isChecked = true
            setOnClickListener {
                idCategoriaActual = null
                cargarRopa(null)
            }
        }
        binding.chipCategorias.addView(chipTodas)

        for (c in categorias) {
            val chip = Chip(this).apply {
                text = c.nombre
                isCheckable = true
                setOnClickListener {
                    idCategoriaActual = c.id
                    cargarRopa(c.id)
                }
            }
            binding.chipCategorias.addView(chip)
        }
    }

    private fun cargarRopa(idCategoria: Int?) {
        adapter.actualizar(ropaDao.listarDisponibles(idCategoria))
    }

    private fun preguntarCantidad(ropa: Ropa) {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Cantidad (Disponible: ${ropa.cantidad})"
        }
        AlertDialog.Builder(this)
            .setTitle(ropa.modelo)
            .setMessage("Disponible: ${ropa.cantidad}")
            .setView(input)
            .setPositiveButton("Agregar") { _, _ ->
                val cant = input.text.toString().trim().toIntOrNull()
                if (cant == null || cant <= 0) {
                    toast("Cantidad inválida"); return@setPositiveButton
                }
                if (cant > ropa.cantidad) {
                    toast("Solo hay ${ropa.cantidad} disponibles"); return@setPositiveButton
                }
                Carrito.agregar(ropa, cant)
                actualizarContador()
                toast("Agregado al carrito")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarContador() {
        binding.tvCarrito.text = "Carrito (${Carrito.cantidadTotal()})"
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        cargarRopa(idCategoriaActual)
        actualizarContador()
    }
}