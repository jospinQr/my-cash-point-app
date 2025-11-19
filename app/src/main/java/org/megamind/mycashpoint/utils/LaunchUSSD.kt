package org.megamind.mycashpoint.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri


fun launchUSSD(context: Context, code: String) {
    val encoded = code.replace("#", Uri.encode("#"))
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$encoded".toUri()
    }
    context.startActivity(intent)
}
