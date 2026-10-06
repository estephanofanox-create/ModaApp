package com.gutierrez.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.data.UsuarioDao
import com.gutierrez.modaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioDao = UsuarioDao(this)

        binding.btnIngresar.setOnClickListener {
            val usuario = binding.etUsuario.text.toString().trim()
            val clave = binding.etClave.text.toString().trim()

            binding.tilUsuario.error = null
            binding.tilClave.error = null

            if (usuario.isEmpty()) binding.tilUsuario.error = "Ingresa el usuario"
            if (clave.isEmpty()) binding.tilClave.error = "Ingresa la contraseña"
            if (usuario.isEmpty() || clave.isEmpty()) return@setOnClickListener

            val u = usuarioDao.validarUsuario(usuario, clave)
            if (u != null) {
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVerCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }
    }
}