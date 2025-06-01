package bd.safety.recipecompose.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun copyPdfFromAssets(context: Context, fileName: String = "sample_document.pdf"): File {
    val outFile = File(context.getExternalFilesDir(null), fileName)
    if (!outFile.exists()) {
        context.assets.open(fileName).use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        }
    }
    return outFile
}
