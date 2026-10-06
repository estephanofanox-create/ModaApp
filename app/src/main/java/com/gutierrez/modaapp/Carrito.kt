package com.gutierrez.modaapp

import com.gutierrez.modaapp.model.Ropa

data class ItemCarrito(
    val ropa: Ropa,
    var cantidad: Int
) {
    val subtotal: Double get() = ropa.precio * cantidad
}

object Carrito {
    val items = mutableListOf<ItemCarrito>()

    fun agregar(ropa: Ropa, cantidad: Int) {
        val existente = items.firstOrNull { it.ropa.id == ropa.id }
        if (existente != null) {
            existente.cantidad += cantidad
        } else {
            items.add(ItemCarrito(ropa, cantidad))
        }
    }

    fun quitar(index: Int) {
        if (index in items.indices) items.removeAt(index)
    }

    fun total(): Double = items.sumOf { it.subtotal }

    fun cantidadTotal(): Int = items.sumOf { it.cantidad }

    fun vaciar() {
        items.clear()
    }
}