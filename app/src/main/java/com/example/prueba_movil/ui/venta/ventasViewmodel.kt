package com.example.prueba_movil.ui.venta


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prueba_movil.data.local.VentaEntity
import com.example.prueba_movil.data.repository.VentaRepository
import com.example.prueba_movil.model.VentaUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VentaViewModel(private val repository: VentaRepository) : ViewModel() {

    // Estado reactivo de la UI para el formulario
    var uiState by mutableStateOf(VentaUiState())
        private set

    // Lista de ventas obtenidas desde Room de forma reactiva (StateFlow)
    val ventas: StateFlow<List<VentaEntity>> = repository.todasLasVentas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Actualizar campos del formulario dinámicamente
    fun onTipoServicioChanged(valor: String) { uiState = uiState.copy(tipoServicio = valor, tipoServicioError = null) }
    fun onFechaOperacionChanged(valor: String) { uiState = uiState.copy(fechaOperacion = valor) }
    fun onClienteChanged(valor: String) { uiState = uiState.copy(cliente = valor) }
    fun onMontoChanged(valor: String) { uiState = uiState.copy(monto = valor, montoError = null) }
    fun onCodigoAutorizacionChanged(valor: String) { uiState = uiState.copy(codigoAutorizacion = valor, codigoError = null) }
    fun onRespaldoUriChanged(valor: String?) { uiState = uiState.copy(respaldoUri = valor) }
    fun onObservacionesChanged(valor: String) { uiState = uiState.copy(observaciones = valor) }

    // Validación y guardado de la venta cumpliendo las reglas del caso
    fun guardarVenta(onSuccess: () -> Unit) {
        val servicioValido = uiState.tipoServicio.isNotBlank()
        val montoDouble = uiState.monto.toDoubleOrNull()
        val montoValido = montoDouble != null && montoDouble > 0.0
        val codigoValido = uiState.codigoAutorizacion.isNotBlank()

        if (!servicioValido || !montoValido || !codigoValido) {
            uiState = uiState.copy(
                tipoServicioError = if (!servicioValido) "El tipo de servicio es obligatorio" else null,
                montoError = if (!montoValido) "Ingrese un monto válido mayor a 0" else null,
                codigoError = if (!codigoValido) "El código de autorización es obligatorio" else null
            )
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(guardando = true)
            val nuevaVenta = VentaEntity(
                tipoServicio = uiState.tipoServicio,
                fechaOperacion = uiState.fechaOperacion.ifBlank { "Fecha actual" },
                cliente = uiState.cliente.ifBlank { "Cliente Ficticio" },
                monto = montoDouble ?: 0.0,
                codigoAutorizacion = uiState.codigoAutorizacion,
                respaldoUri = uiState.respaldoUri,
                estado = "Borrador",
                observaciones = uiState.observaciones
            )
            repository.insertar(nuevaVenta)
            uiState = VentaUiState() // Reiniciar formulario
            onSuccess()
        }
    }
}

// Factory para instanciar el ViewModel inyectando el Repositorio
class VentaViewModelFactory(private val repository: VentaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VentaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VentaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}