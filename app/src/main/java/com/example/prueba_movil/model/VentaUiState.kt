package com.example.prueba_movil.model

data class VentaUiState(
    val tipoServicio: String = "",
    val fechaOperacion: String = "",
    val cliente: String = "",
    val monto: String = "",
    val codigoAutorizacion: String = "",
    val respaldoUri: String? = null,
    val estado: String = "Borrador",
    val observaciones: String = "",
    val tipoServicioError: String? = null,
    val montoError: String? = null,
    val codigoError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false
)