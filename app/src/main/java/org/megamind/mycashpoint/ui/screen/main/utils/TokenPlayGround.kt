package org.megamind.mycashpoint.ui.screen.main.utils

import android.util.Base64
import org.json.JSONObject

fun decodeJwtPayload(token: String): JSONObject {
    val parts = token.split(".")
    if (parts.size != 3) throw IllegalArgumentException("Token invalid")

    val payload = parts[1]
    val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)

    return JSONObject(String(decoded))
}