package org.megamind.mycashpoint.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Report
import androidx.compose.ui.graphics.vector.ImageVector
import org.megamind.mycashpoint.ui.navigation.Destination

data class MenuItem(

    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String,

    )


val navBarItem = listOf(

    MenuItem(
        title = "Transaction",
        selectedIcon = Icons.Filled.CloudSync,
        unSelectedIcon = Icons.Outlined.CloudSync,
        route = Destination.OPERATEUR.name
    ),

    MenuItem(
        title = "Caisse",
        selectedIcon = Icons.Filled.MonetizationOn,
        unSelectedIcon = Icons.Outlined.MonetizationOn,
        route = Destination.CAISSE.name
    ),


    MenuItem(
        title = "Rapport",
        selectedIcon = Icons.Filled.Report,
        unSelectedIcon = Icons.Outlined.Report,
        route = Destination.RAPPORT.name
    ),
)
