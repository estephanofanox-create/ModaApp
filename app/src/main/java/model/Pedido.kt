package com.gutierrez.modaapp.model

data class Pedido(
    val id: Int,
    val idCliente: Int,
    val fecha: String,
    val total: Double,
    val estado: String,
    val fechaAtencion: String?
)