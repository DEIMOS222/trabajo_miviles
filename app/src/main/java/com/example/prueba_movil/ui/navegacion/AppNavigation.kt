package com.example.prueba_movil.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba_movil.ui.venta.ListaVentasScreen
import com.example.prueba_movil.ui.venta.VentaFormScreen
import com.example.prueba_movil.ui.venta.VentaViewModel

sealed class Screen(val route: String) {
    object Lista : Screen("lista_ventas")
    object Formulario : Screen("formulario_venta")
}

@Composable
fun AppNavigation(
    viewModel: VentaViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        composable(Screen.Lista.route) {
            val ventas by viewModel.ventas.collectAsState()
            ListaVentasScreen(
                ventas = ventas,
                onAgregarClick = { navController.navigate(Screen.Formulario.route) }
            )
        }
        composable(Screen.Formulario.route) {
            VentaFormScreen(
                viewModel = viewModel,
                onVentaGuardada = { navController.popBackStack() },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}