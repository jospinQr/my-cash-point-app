package org.megamind.mycashpoint.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object Constants {

    enum class Devise {
        USD,
        CDF
    }


    /**
     * Convertit un timestamp Long (en millisecondes) en une chaîne de date formatée.
     *
     * @param timestamp Le temps en millisecondes depuis l'époque (par ex. System.currentTimeMillis()).
     * @param pattern Le format de sortie souhaité (par ex. "dd/MM/yyyy HH:mm").
     * @return Une chaîne de caractères représentant la date et l'heure formatées, ou une chaîne vide si le timestamp est null.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestamp(timestamp: Long?, pattern: String = "dd/MM/yyyy HH:mm"): String {
        if (timestamp == null) return "" // Gère le cas où le timestamp pourrait être null

        // Crée un Instant à partir du timestamp
        val instant = Instant.ofEpochMilli(timestamp)

        // Convertit l'Instant en une date/heure locale dans le fuseau horaire de l'appareil
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

        // Crée un formateur avec votre pattern personnalisé
        val formatter = DateTimeFormatter.ofPattern(pattern)

        // Formate et retourne la date
        return localDateTime.format(formatter)
    }

}