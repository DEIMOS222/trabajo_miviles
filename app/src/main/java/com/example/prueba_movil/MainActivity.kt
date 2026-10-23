package com.example.prueba_movil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.prueba_movil.data.local.AppDatabase
import com.example.prueba_movil.data.repository.VentaRepository
import com.example.prueba_movil.ui.navigation.AppNavigation
import com.example.prueba_movil.ui.theme.Prueba_movilTheme
import com.example.prueba_movil.ui.venta.VentaViewModel
import com.example.prueba_movil.ui.venta.VentaViewModelFactory

class MainActivity : ComponentActivity() {

    private val db by lazy { AppDatabase.obtenerBaseDeDatos(this) }
    private val repository by lazy { VentaRepository(db.ventaDao()) }
    private val viewModel: VentaViewModel by viewModels(factoryProducer = { VentaViewModelFactory(repository) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Prueba_movilTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}