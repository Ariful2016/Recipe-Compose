package bd.safety.recipecompose.ui.screens.printerScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bd.safety.recipecompose.models.PrintState
import bd.safety.recipecompose.viewmodels.printer.PrinterViewModel
import kotlinx.coroutines.launch

@Composable
fun PrinterScreen(navController: NavController, viewModel: PrinterViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val printState by viewModel.printState.collectAsState()
    val scope = rememberCoroutineScope()

    // Permission launcher
    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // Permissions granted
        } else {
            scope.launch {
                viewModel.setPrintState(PrintState.Error("Permissions denied"))
            }
        }
    }

    // Check and request permissions
    LaunchedEffect(Unit) {
        val permissions = buildList {
            add(Manifest.permission.INTERNET)
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.BLUETOOTH_ADMIN)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_SCAN)
                add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissions.isNotEmpty()) {
            permissionsLauncher.launch(permissions)
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (val state = printState) {
                is PrintState.Idle -> "Ready to print"
                is PrintState.Loading -> "Printing..."
                is PrintState.Success -> state.message
                is PrintState.Error -> state.message
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val imagePath = "${context.getExternalFilesDir(null)}/sample_image.png"
                viewModel.printImage(imagePath)
            },
            enabled = printState !is PrintState.Loading
        ) {
            Text("Print Image")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val pdfPath = "${context.getExternalFilesDir(null)}/sample_document.pdf"
                viewModel.printPDF(pdfPath)
            },
            enabled = printState !is PrintState.Loading
        ) {
            Text("Print PDF")
        }
    }
}