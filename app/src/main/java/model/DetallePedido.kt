package com.gutierrez.modaapp.model

data class DetallePedido(
    val id: Int,
    val idPedido: Int,
    val idRopa: Int,
    val cantidad: Int,
    val precioUnit: Double,
    val subtotal: Double
)