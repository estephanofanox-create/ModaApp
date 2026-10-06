package com.gutierrez.modaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.data.ReporteDao
import com.gutierrez.modaapp.databinding.ItemClienteBinding

class ClienteAdapter(
    private var lista: List<ReporteDao.ClienteItem>
) : RecyclerView.Adapter<ClienteAdapter.ClienteVH>() {

    inner class ClienteVH(val binding: ItemClienteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteVH {
        val binding = ItemClienteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClienteVH(binding)
    }

    override fun onBindViewHolder(holder: ClienteVH, position: Int) {
        val c = lista[position]
        holder.binding.tvNombre.text = "${c.nombres} ${c.apellidos}"
        holder.binding.tvTelefono.text = c.telefono
        holder.binding.tvPedidos.text = "${c.pedidos} pedidos"
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<ReporteDao.ClienteItem>) {
        lista = nueva
        notifyDataSetChanged()
    }
}