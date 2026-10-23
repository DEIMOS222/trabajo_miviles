package com.example.prueba_movil.data.repository

import com.example.prueba_movil.model.QrResult

class QrRepository {
    fun processQrContent(content: String): QrResult {
        return QrResult(content)
    }
}