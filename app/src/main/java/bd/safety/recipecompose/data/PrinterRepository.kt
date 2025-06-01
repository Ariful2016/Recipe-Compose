package bd.safety.recipecompose.data

import bd.safety.recipecompose.data.manager.BrotherPrinterManager
import bd.safety.recipecompose.models.PrintState
import com.brother.ptouch.sdk.PrinterInfo
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ViewModelScoped
class PrinterRepository @Inject constructor(
    private val printerManager: BrotherPrinterManager
) {
    private val _printState = MutableStateFlow<PrintState>(PrintState.Idle)
    val printState: StateFlow<PrintState> = _printState.asStateFlow()

    suspend fun printImage(imagePath: String) {
        _printState.value = PrintState.Loading
        val result = printerManager.printImage(imagePath)
        _printState.value = when {
            result.isSuccess -> {
                val status = result.getOrNull()
                if (status?.errorCode == PrinterInfo.ErrorCode.ERROR_NONE) {
                    PrintState.Success("Image print successful")
                } else {
                    PrintState.Error("Print error: ${status?.errorCode}")
                }
            }
            else -> PrintState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    suspend fun printPDF(pdfPath: String, page: Int = 1) {
        _printState.value = PrintState.Loading
        val result = printerManager.printPDF(pdfPath, page)
        _printState.value = when {
            result.isSuccess -> {
                val status = result.getOrNull()
                if (status?.errorCode == PrinterInfo.ErrorCode.ERROR_NONE) {
                    PrintState.Success("PDF print successful")
                } else {
                    PrintState.Error("Print error: ${status?.errorCode}")
                }
            }
            else -> PrintState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }
}