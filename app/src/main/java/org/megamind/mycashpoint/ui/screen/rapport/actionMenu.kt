package org.megamind.mycashpoint.ui.screen.rapport

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionMenu (
    val text: String,

)

val actionMenus = listOf(

    ActionMenu("Envoyer uniquement les transactions"),
    ActionMenu("Envoyer uniquement les solde"),
    ActionMenu("Envoyer tout à la fois"),


)


