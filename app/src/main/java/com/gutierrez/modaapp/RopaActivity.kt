package com.gutierrez.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            // Por ahora nada (HU-07 lo usará)
        }
        binding.rvRopa.layoutManager = LinearLayoutManager(this)
        binding.rvRopa.adapter = adapter

        binding.btnNueva.setOnClickListener {
            startActivity(Intent(this, RopaFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizar(ropaDao.listar())
    }
}