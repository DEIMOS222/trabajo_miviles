package com.example.prueba_movil.data.local

package com.example.prueba_movil.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Query("SELECT * FROM tabla_ventas")
    fun obtenerTodasLasVentas(): Flow<List<VentaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVenta(venta: VentaEntity)

    @Update
    suspend fun actualizarVenta(venta: VentaEntity)
}