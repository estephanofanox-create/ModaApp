package com.gutierrez.modaapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gutierrez.modaapp.data.ReporteDao
import com.gutierrez.modaapp.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding
    private lateinit var reporteDao: ReporteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reporteDao = ReporteDao(this)

        val (cantidad, monto) = reporteDao.atendidosDelMes()
        binding.tvMonto.text = "S/ %.2f".format(monto)
        binding.tvAtendidos.text = "$cantidad pedidos atendidos"

        binding.listaStock.removeAllViews()
        for (s in reporteDao.stockPorPrenda()) {
            val tv = TextView(this).apply {
                text = "${s.modelo}  —  ${s.cantidad}"
                textSize = 14f
                setPadding(8, 20, 8, 20)
                gravity = Gravity.START
                setTextColor(if (s.cantidad <= 3) Color.parseColor("#C2185B") else Color.parseColor("#1A1A1A"))
                if (s.cantidad <= 3) setBackgroundColor(Color.parseColor("#FCE4EC"))
            }
            binding.listaStock.addView(tv)
        }
    }
}