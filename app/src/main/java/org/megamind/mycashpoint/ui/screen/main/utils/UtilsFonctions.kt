package org.megamind.mycashpoint.ui.screen.main.utils

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