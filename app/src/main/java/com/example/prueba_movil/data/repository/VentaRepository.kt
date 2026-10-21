package com.example.prueba_movil.data.repository



import com.example.prueba_movil.data.local.VentaDao
import com.example.prueba_movil.data.local.VentaEntity
import kotlinx.coroutines.flow.Flow

class VentaRepository(private val ventaDao: VentaDao) {

    // Obtiene todas las ventas de forma reactiva
    val todasLasVentas: Flow<List<VentaEntity>> = ventaDao.obtenerTodasLasVentas()

    // Inserta una nueva venta en la base de datos local
    suspend fun insertar(venta: VentaEntity) {
        ventaDao.insertarVenta(venta)
    }

    // Actualiza una venta existente
    suspend fun actualizar(venta: VentaEntity) {
        ventaDao.actualizarVenta(venta)
    }
}