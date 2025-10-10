package org.megamind.mycashpoint.utils

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