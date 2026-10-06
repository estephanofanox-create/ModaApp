package com.gutierrez.modaapp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.data.PedidoDao
import com.gutierrez.modaapp.databinding.ItemDetallePedidoBinding
import java.io.File

class DetallePedidoAdapter(
    private val lista: List<PedidoDao.DetalleItem>
) : RecyclerView.Adapter<DetallePedidoAdapter.DetalleVH>() {

    inner class DetalleVH(val binding: ItemDetallePedidoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleVH {
        val binding = ItemDetallePedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetalleVH(binding)
    }

    override fun onBindViewHolder(holder: DetalleVH, position: Int) {
        val d = lista[position]
        holder.binding.tvModelo.text = d.modelo
        holder.binding.tvDetalle.text = "${d.talla} · ${d.color}"
        holder.binding.tvCantidad.text = "x${d.cantidad}"

        val f = File(d.foto)
        if (f.exists()) holder.binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(f.absolutePath))
        else holder.binding.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    override fun getItemCount() = lista.size
}