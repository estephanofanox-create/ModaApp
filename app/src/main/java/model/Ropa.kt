package com.gutierrez.modaapp.model

data class Ropa(
    val id: Int,
    val modelo: String,
    val idCategoria: Int,
    val talla: String,
    val marca: String,
    val color: String,
    val precio: Double,
    val cantidad: Int,
    val foto: String
)