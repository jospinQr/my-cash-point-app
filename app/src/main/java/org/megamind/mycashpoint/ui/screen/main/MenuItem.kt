package org.megamind.mycashpoint.ui.screen.main

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Report
import androidx.compose.ui.graphics.vector.ImageVector
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.ui.navigation.Destination

data class MenuItem(

    val title: String,
    @DrawableRes
    val selectedIcon: Int,
    val unSelectedIcon: Int,
    val route: String,

    )


val navBarItem = listOf(

    MenuItem(
        title = "Transaction",
        selectedIcon = R.drawable.transaction_fill,
        unSelectedIcon = R.drawable.transaction_out,
        route = Destination.OPERATEUR.name
    ),

    MenuItem(
        title = "Solde",
        selectedIcon = R.drawable.solde_fill,
        unSelectedIcon = R.drawable.solde_out,
        route = Destination.CAISSE.name
    ),


    MenuItem(
        title = "Rapport",
        selectedIcon = R.drawable.rapport_fill,
        unSelectedIcon = R.drawable.rapport_out,
        route = Destination.RAPPORT.name
    ),
)
