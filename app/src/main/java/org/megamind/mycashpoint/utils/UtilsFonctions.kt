package org.megamind.mycashpoint.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.io.File
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.regex.Pattern
import kotlin.text.isNullOrBlank

class UtilsFonctions {


    companion object {


        fun isValidEmail(email: String?): Boolean {
            // Handle null or empty upfront for minor optimization
            if (email.isNullOrBlank()) {
                return false
            }
            val emailRegex = "^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
            val pattern = Pattern.compile(emailRegex)
            return pattern.matcher(email).matches()
        }


        fun openPdfFile(context: Context, bytes: ByteArray) {
            val file = File(context.cacheDir, "report.pdf")
            file.writeBytes(bytes)

            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Aucune application PDF installée", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        /**
         * Opens an Excel file from ByteArray
         * @param context The context
         * @param bytes The Excel file content as ByteArray
         * @param fileName Optional file name (default: "report.xlsx")
         */
        fun openExcelFile(context: Context, bytes: ByteArray, fileName: String = "report.xlsx") {
            val file = File(context.cacheDir, fileName)
            file.writeBytes(bytes)

            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Aucune application Excel installée", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}

fun String.toBigDecimalOrNull(): BigDecimal? {

    return try {
        BigDecimal(this)
    } catch (e: Exception) {
        null
    }
}


fun BigDecimal.toMontant(
    avecDecimales: Boolean = true,
    symboleDevise: String = "",

    ): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }

    val pattern = if (avecDecimales) "#,##0.00" else "#,##0"
    val formatter = DecimalFormat(pattern, symbols)
    val montantFormate = formatter.format(this)

    return if (symboleDevise.isNotEmpty()) {
        "$montantFormate $symboleDevise"
    } else {
        montantFormate
    }
}


fun Long.toLocalDate(
    zoneId: ZoneId = ZoneId.systemDefault()
): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
}
