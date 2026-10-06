package com.gutierrez.modaapp

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.databinding.ItemCatalogoBinding
import com.gutierrez.modaapp.model.Ropa
import java.io.File

class CatalogoAdapter(
    private var lista: List<Ropa>,
    private val onAgregar: (Ropa) -> Unit
) : RecyclerView.Adapter<CatalogoAdapter.CatalogoVH>() {

    inner class CatalogoVH(val binding: ItemCatalogoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogoVH {
        val binding = ItemCatalogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatalogoVH(binding)
    }

    override fun onBindViewHolder(holder: CatalogoVH, position: Int) {
        val r = lista[position]
        holder.binding.tvModelo.text = r.modelo
        holder.binding.tvDetalle.text = "${r.talla} · ${r.color}"
        holder.binding.tvPrecio.text = "S/ %.2f".format(r.precio)

        val file = File(r.foto)
        if (file.exists()) {
            holder.binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        } else {
            holder.binding.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener { onAgregar(r) }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Ropa>) {
        lista = nueva
        notifyDataSetChanged()
    }
}