package com.example.prueba_movil.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_ventas")
data class VentaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val tipoServicio: String,
    val fechaOperacion: String,
    val cliente: String,
    val monto: Double,
    val codigoAutorizacion: String,
    val respaldoUri: String? = null,
    val estado: String = "Borrador",
    val observaciones: String = ""
)