package org.megamind.mycashpoint.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.navigation.MyNavHost
import kotlin.collections.contains


@Composable
fun MyCashPointApp() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Déterminer si on doit afficher le BottomBar
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.route in listOf(
            Destination.OPERATEUR.name,
            Destination.CAISSE.name,
            Destination.RAPPORT.name
        )
    } == true
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),

                ) {


                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .06f),
                    contentColor = MaterialTheme.colorScheme.primary,
                    tonalElevation = 10.dp
                )
                {

                    navBarItem.forEachIndexed { index, item ->

                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        }
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),

                                ),
                            selected = selected == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = {
                                Text(
                                    item.title,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = if (selected == true) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = {

                                if (selected == true) {
                                    Crossfade(
                                        targetState = index,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioHighBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    ) { index ->
                                        Icon(
                                            imageVector = item.selectedIcon,
                                            contentDescription = null,

                                            )
                                    }
                                } else {
                                    Crossfade(targetState = item) {
                                        Icon(
                                            imageVector = it.unSelectedIcon,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )


                    }

                }
            }


        }


    ) { innerPadding ->


        MyNavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController
        )

    }
}
