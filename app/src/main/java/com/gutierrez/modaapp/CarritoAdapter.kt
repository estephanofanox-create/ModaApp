package com.gutierrez.modaapp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.databinding.ItemCarritoBinding
import java.io.File

class CarritoAdapter(
    private val lista: List<ItemCarrito>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoVH>() {

    inner class CarritoVH(val binding: ItemCarritoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoVH {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoVH(binding)
    }

    override fun onBindViewHolder(holder: CarritoVH, position: Int) {
        val item = lista[position]
        holder.binding.tvModelo.text = item.ropa.modelo
        holder.binding.tvDetalle.text = "${item.ropa.talla} · x${item.cantidad}"
        holder.binding.tvSubtotal.text = "S/ %.2f".format(item.subtotal)

        val file = File(item.ropa.foto)
        if (file.exists()) {
            holder.binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        } else {
            holder.binding.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount() = lista.size
}