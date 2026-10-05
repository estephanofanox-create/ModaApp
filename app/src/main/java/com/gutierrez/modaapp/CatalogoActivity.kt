package com.gutierrez.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.databinding.ActivityCatalogoBinding

class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatalogoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}