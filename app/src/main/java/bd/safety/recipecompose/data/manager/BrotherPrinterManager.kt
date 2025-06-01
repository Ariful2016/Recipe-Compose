package bd.safety.recipecompose.data.manager
import android.content.Context
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.ptouch.sdk.PrinterStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrotherPrinterManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val printer = Printer()

    init {
        val settings = printer.getPrinterInfo().apply {
            printerModel = PrinterInfo.Model.QL_820NWB // Replace with your printer model
            port = PrinterInfo.Port.NET // Use NET for Wi-Fi, BLUETOOTH for Bluetooth, or USB
            ipAddress = "192.168.1.100" // Replace with your printer's IP address
            workPath = context.getExternalFilesDir(null)?.absolutePath ?: ""
            printMode = PrinterInfo.PrintMode.FIT_TO_PAGE
            paperSize = PrinterInfo.PaperSize.A4
        }
        printer.setPrinterInfo(settings)
    }

    suspend fun printImage(imagePath: String): Result<PrinterStatus> = withContext(Dispatchers.IO) {
        try {
            val file = File(imagePath)
            if (!file.exists()) {
                return@withContext Result.failure(Exception("Image file not found"))
            }
            if (printer.startCommunication()) {
                val result = printer.printFile(imagePath)
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to connect to printer"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            printer.endCommunication()
        }
    }

    suspend fun printPDF(pdfPath: String, page: Int = 1): Result<PrinterStatus> = withContext(Dispatchers.IO) {
        try {
            val file = File(pdfPath)
            if (!file.exists()) {
                return@withContext Result.failure(Exception("PDF file not found"))
            }
            if (printer.startCommunication()) {
                val result = printer.printPDFWithURL(file.toURI().toString(), page)
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to connect to printer"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            printer.endCommunication()
        }
    }
}