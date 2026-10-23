package com.example.prueba_movil.ui.navigation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba_movil.ui.venta.ListaVentasScreen
import com.example.prueba_movil.ui.venta.QrScannerScreen
import com.example.prueba_movil.ui.venta.QrViewModel
import com.example.prueba_movil.ui.venta.VentaFormScreen
import com.example.prueba_movil.ui.venta.VentaViewModel
import com.example.prueba_movil.utils.CameraPermissionHelper

sealed class Screen(val route: String) {
    object Lista : Screen("lista_ventas")
    object Formulario : Screen("formulario_venta")
    object QrScanner : Screen("qr_scanner")
}

@Composable
fun AppNavigation(
    viewModel: VentaViewModel,
    qrViewModel: QrViewModel = viewModel(),
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
                onVolver = { navController.popBackStack() },
                onEscanearQr = { navController.navigate(Screen.QrScanner.route) }
            )
        }
        composable(Screen.QrScanner.route) {
            val context = LocalContext.current
            var hasPermission by remember { mutableStateOf(CameraPermissionHelper.hasCameraPermission(context)) }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                hasPermission = isGranted
            }

            val qrResult by qrViewModel.qrResult.collectAsState()
            LaunchedEffect(qrResult) {
                qrResult?.let { qr ->
                    viewModel.onCodigoAutorizacionChanged(qr.content)
                    qrViewModel.clearResult()
                    navController.popBackStack()
                }
            }

            QrScannerScreen(
                hasCameraPermission = hasPermission,
                onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                onQrDetected = { qrContent ->
                    qrViewModel.onQrDetected(qrContent)
                },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}