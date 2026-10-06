package com.gutierrez.modaapp

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.data.CategoriaDao
import com.gutierrez.modaapp.data.FotoUtils
import com.gutierrez.modaapp.data.RopaDao
import com.gutierrez.modaapp.databinding.ActivityRopaFormBinding
import com.gutierrez.modaapp.model.Categoria
import com.gutierrez.modaapp.model.Ropa
import java.io.File

class RopaFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRopaFormBinding
    private lateinit var categoriaDao: CategoriaDao
    private lateinit var ropaDao: RopaDao
    private var categorias: List<Categoria> = emptyList()
    private var rutaFoto: String? = null

    private val pickFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            val ruta = FotoUtils.copiarAInterno(this, uri)
            if (ruta != null) {
                rutaFoto = ruta
                binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(File(ruta).absolutePath))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRopaFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoriaDao = CategoriaDao(this)
        ropaDao = RopaDao(this)

        categorias = categoriaDao.listar()
        binding.spCategoria.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias.map { it.nombre }
        )

        val tallas = listOf("XS", "S", "M", "L", "XL")
        binding.spTalla.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            tallas
        )

        binding.btnElegirFoto.setOnClickListener {
            pickFoto.launch(
                androidx.activity.result.PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        binding.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        val modelo = binding.etModelo.text.toString().trim()
        val marca = binding.etMarca.text.toString().trim()
        val color = binding.etColor.text.toString().trim()
        val precioTxt = binding.etPrecio.text.toString().trim()
        val cantidadTxt = binding.etCantidad.text.toString().trim()

        if (modelo.isEmpty()) { toast("Ingresa el modelo"); return }
        if (rutaFoto == null) { toast("Elige una foto"); return }
        if (precioTxt.isEmpty()) { toast("Ingresa el precio"); return }
        if (cantidadTxt.isEmpty()) { toast("Ingresa la cantidad"); return }

        val precio = precioTxt.toDoubleOrNull() ?: 0.0
        val cantidad = cantidadTxt.toIntOrNull() ?: 0

        if (precio <= 0) { toast("El precio debe ser mayor a 0"); return }
        if (cantidad < 0) { toast("La cantidad no puede ser negativa"); return }

        val ropa = Ropa(
            id = 0,
            modelo = modelo,
            idCategoria = categorias[binding.spCategoria.selectedItemPosition].id,
            talla = binding.spTalla.selectedItem.toString(),
            marca = marca,
            color = color,
            precio = precio,
            cantidad = cantidad,
            foto = rutaFoto!!
        )

        val id = ropaDao.insertar(ropa)
        if (id > 0) {
            Toast.makeText(this, "Prenda guardada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            toast("Error al guardar")
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}