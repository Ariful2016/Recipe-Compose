package bd.safety.recipecompose.viewmodels.printer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.safety.recipecompose.data.printer.PrinterRepository
import bd.safety.recipecompose.models.PrintState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrinterViewModel @Inject constructor(
    private val printerRepository: PrinterRepository
) : ViewModel() {
    val printState = printerRepository.printState

    fun printImage(imagePath: String) {
        viewModelScope.launch {
            printerRepository.printImage(imagePath)
        }
    }

    fun printPDF(pdfPath: String, page: Int = 1) {
        viewModelScope.launch {
            printerRepository.printPDF(pdfPath, page)
        }
    }

    fun setPrintState(state: PrintState) {
        viewModelScope.launch {
            printerRepository.setPrintState(state)
        }
    }
}