package com.gutierrez.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.gutierrez.modaapp.data.RopaDao
import com.gutierrez.modaapp.databinding.ActivityRopaBinding

class RopaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRopaBinding
    private lateinit var ropaDao: RopaDao
    private lateinit var adapter: RopaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ropaDao = RopaDao(this)

        adapter = RopaAdapter(emptyList()) { ropa ->
            val i = Intent(this, RopaFormActivity::class.java)
            i.putExtra("id", ropa.id)
            startActivity(i)
        }
        binding.rvRopa.layoutManager = LinearLayoutManager(this)
        binding.rvRopa.adapter = adapter

        binding.btnNueva.setOnClickListener {
            startActivity(Intent(this, RopaFormActivity::class.java))
        }

        binding.etBuscar.doAfterTextChanged { texto ->
            val q = texto?.toString()?.trim().orEmpty()
            if (q.isEmpty()) adapter.actualizar(ropaDao.listar())
            else adapter.actualizar(ropaDao.listarConFiltro(q))
        }
    }

    override fun onResume() {
        super.onResume()
        val q = binding.etBuscar.text?.toString()?.trim().orEmpty()
        if (q.isEmpty()) adapter.actualizar(ropaDao.listar())
        else adapter.actualizar(ropaDao.listarConFiltro(q))
    }
}