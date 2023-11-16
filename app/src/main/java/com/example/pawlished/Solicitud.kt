package com.example.pawlished


data class Solicitud(
    val id: String,
    val direccion: String,
    val descripcion: String,
    val servicios: List<String>,
    val numero: String,
    val precio: Double

)
