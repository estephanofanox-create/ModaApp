package com.gutierrez.modaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.gutierrez.modaapp.data.CategoriaDao
import com.gutierrez.modaapp.data.RopaDao
import com.gutierrez.modaapp.databinding.ActivityCatalogoBinding
import com.gutierrez.modaapp.model.Categoria

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

        adapter = CatalogoAdapter(emptyList()) { ropa ->
            Toast.makeText(this, "Tocaste: ${ropa.modelo}", Toast.LENGTH_SHORT).show()
        }
        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter

        categorias = categoriaDao.listar()
        armarChips()
        cargarRopa(null)
    }

    private fun armarChips() {
        // Chip "Todas"
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

    override fun onResume() {
        super.onResume()
        cargarRopa(idCategoriaActual)
    }
}