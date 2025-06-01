package bd.safety.recipecompose.data.manager
import android.content.Context
import android.graphics.Bitmap
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.ptouch.sdk.PrinterStatus
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.PDFRenderer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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
            ipAddress = "192.168.10.1_1" // Replace with your printer's IP address
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
            val pdfFile = File(pdfPath)
            if (!pdfFile.exists()) {
                return@withContext Result.failure(Exception("PDF file not found"))
            }

            // Convert PDF page to bitmap
            val document = PDDocument.load(pdfFile)
            if (page < 1 || page > document.numberOfPages) {
                document.close()
                return@withContext Result.failure(Exception("Invalid page number"))
            }

            val pdfRenderer = PDFRenderer(document)
            val bitmap = pdfRenderer.renderImageWithDPI(page - 1, 300f) // 300 DPI for high quality
            document.close()

            // Save bitmap to a temporary file
            val tempImageFile = File(context.getExternalFilesDir(null), "temp_print_image.png")
            FileOutputStream(tempImageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            // Print the bitmap
            if (printer.startCommunication()) {
                val result = printer.printFile(tempImageFile.absolutePath)
                tempImageFile.delete() // Clean up temporary file
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