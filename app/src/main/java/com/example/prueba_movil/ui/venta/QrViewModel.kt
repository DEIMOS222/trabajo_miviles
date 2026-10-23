package com.example.prueba_movil.ui.venta

import androidx.lifecycle.ViewModel
import com.example.prueba_movil.data.repository.QrRepository
import com.example.prueba_movil.model.QrResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QrViewModel : ViewModel() {
    private val repository = QrRepository()
    private val _qrResult = MutableStateFlow<QrResult?>(null)
    val qrResult: StateFlow<QrResult?> = _qrResult.asStateFlow()

    fun onQrDetected(content: String) {
        _qrResult.value = repository.processQrContent(content)
    }

    fun clearResult() {
        _qrResult.value = null
    }
}