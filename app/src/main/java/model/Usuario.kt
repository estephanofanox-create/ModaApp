package com.gutierrez.modaapp.model

data class Usuario(
    val id: Int,
    val usuario: String,
    val clave: String,
    val rol: String,
    val telefono: String
)