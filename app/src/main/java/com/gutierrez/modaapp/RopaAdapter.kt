package com.gutierrez.modaapp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.databinding.ItemRopaBinding
import com.gutierrez.modaapp.model.Ropa
import java.io.File

class RopaAdapter(
    private var lista: List<Ropa>,
    private val onClick: (Ropa) -> Unit
) : RecyclerView.Adapter<RopaAdapter.RopaVH>() {

    inner class RopaVH(val binding: ItemRopaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RopaVH {
        val binding = ItemRopaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RopaVH(binding)
    }

    override fun onBindViewHolder(holder: RopaVH, position: Int) {
        val r = lista[position]
        holder.binding.tvModelo.text = r.modelo
        holder.binding.tvDetalle.text = "${r.talla} · ${r.color}"
        holder.binding.tvPrecio.text = "S/ %.2f".format(r.precio)
        holder.binding.tvStock.text = "Stock: ${r.cantidad}"

        val file = File(r.foto)
        if (file.exists()) {
            holder.binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        } else {
            holder.binding.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener { onClick(r) }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Ropa>) {
        lista = nueva
        notifyDataSetChanged()
    }
}