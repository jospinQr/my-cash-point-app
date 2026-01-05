package org.megamind.mycashpoint.ui.screen.main

import androidx.annotation.DrawableRes
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.ui.navigation.Destination

data class MenuItem(

    val title: String,
    @DrawableRes val selectedIcon: Int,
    val unSelectedIcon: Int,
    val route: String,
)


val agentNavBarItem = listOf(

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


val adminNavBarItem = listOf(

    MenuItem(
        title = "Tableau de bord",
        selectedIcon = R.drawable.dash_out,
        unSelectedIcon = R.drawable.dash_fill,
        route = Destination.DASHBOARD.name
    ),
//
//    MenuItem(
//        title = "Transactions",
//        selectedIcon = R.drawable.transaction_fill,
//        unSelectedIcon = R.drawable.transaction_out,
//        route = Destination.ADMIN_TRANSACT.name
//    ),
//
//    MenuItem(
//        title = "Mouvements caisse",
//        selectedIcon = R.drawable.depot,
//        unSelectedIcon = R.drawable.depot,
//        route = Destination.MOUVEMENT.name
//    ),


    MenuItem(
        title = "Rapport",
        selectedIcon = R.drawable.rapport_fill,
        unSelectedIcon = R.drawable.rapport_out,
        route = Destination.ADMIN_REPPORT.name
    )


)

