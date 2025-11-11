package org.megamind.mycashpoint.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.megamind.mycashpoint.R

data class Operateur(

    val id: Int = 0,
    val name: String = "",
    @DrawableRes
    val logo: Int = 0,
    val color: Color
)


val operateurs = listOf(

    Operateur(
        id = 1,
        name = "Airtel Money",
        logo = R.drawable.airtel_logo,
        color = Color(0xFFE4002B) // Rouge Airtel officiel
    ),
    Operateur(
        id = 2,
        name = "Orange Money",
        logo = R.drawable.orange_logo,
        color = Color(0xFFFF6600) // Orange officiel
    ),
    Operateur(
        id = 3,
        name = "Vodacom M-Pesa",
        logo = R.drawable.vodacom_logo,
        color = Color(0xFF008043) // Vert M-Pesa (Vodacom Money)
    ),
    Operateur(
        id = 4,
        name = "Equity BCBC",
        logo = R.drawable.bcdc,
        color = Color(0xFF5B009C) // Violet Africell
    )

)