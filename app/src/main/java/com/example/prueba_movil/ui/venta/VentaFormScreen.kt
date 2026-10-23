package com.example.prueba_movil.ui.venta

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaFormScreen(
    viewModel: VentaViewModel,
    onVentaGuardada: () -> Unit,
    onVolver: () -> Unit
) {
    val uiState = viewModel.uiState

    // Recurso Nativo: Selector de imágenes de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onRespaldoUriChanged(uri?.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nueva Venta") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Tipo de Servicio / Producto
            OutlinedTextField(
                value = uiState.tipoServicio,
                onValueChange = { viewModel.onTipoServicioChanged(it) },
                label = { Text("Tipo de Servicio / Producto") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                isError = uiState.tipoServicioError != null,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.tipoServicioError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Campo Cliente
            OutlinedTextField(
                value = uiState.cliente,
                onValueChange = { viewModel.onClienteChanged(it) },
                label = { Text("Nombre del Cliente") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Monto
            OutlinedTextField(
                value = uiState.monto,
                onValueChange = { viewModel.onMontoChanged(it) },
                label = { Text("Monto ($)") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.montoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.montoError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Campo Código de Autorización
            OutlinedTextField(
                value = uiState.codigoAutorizacion,
                onValueChange = { viewModel.onCodigoAutorizacionChanged(it) },
                label = { Text("Código de Autorización") },
                leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
                isError = uiState.codigoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.codigoError?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Campo Observaciones
            OutlinedTextField(
                value = uiState.observaciones,
                onValueChange = { viewModel.onObservacionesChanged(it) },
                label = { Text("Observaciones") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            // Recurso Nativo: Adjuntar comprobante / foto
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Comprobante de Venta (Recurso Nativo)", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.respaldoUri?.let { uriString ->
                        Image(
                            painter = rememberAsyncImagePainter(uriString),
                            contentDescription = "Comprobante",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (uiState.respaldoUri == null) "Seleccionar Foto" else "Cambiar Foto")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Guardar
            Button(
                onClick = {
                    viewModel.guardarVenta(onSuccess = onVentaGuardada)
                },
                enabled = !uiState.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.guardando) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Venta", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}