package com.gutierrez.modaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gutierrez.modaapp.databinding.ItemPedidoBinding

class PedidoAdapter(
    private var lista: List<Triple<Int, String, Double>>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.PedidoVH>() {

    inner class PedidoVH(val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoVH {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoVH(binding)
    }

    override fun onBindViewHolder(holder: PedidoVH, position: Int) {
        val (id, cliente, total) = lista[position]
        holder.binding.tvNumero.text = "Pedido #$id"
        holder.binding.tvCliente.text = cliente
        holder.binding.tvTotal.text = "S/ %.2f".format(total)

        holder.itemView.setOnClickListener { onClick(id) }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Triple<Int, String, Double>>) {
        lista = nueva
        notifyDataSetChanged()
    }
}