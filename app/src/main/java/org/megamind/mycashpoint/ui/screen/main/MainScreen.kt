package org.megamind.mycashpoint.ui.screen.main

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.megamind.mycashpoint.ui.component.CustomSnackbar
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.navigation.MyNavHost
import kotlin.collections.contains


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyCashPointApp() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }

    // Déterminer si on doit afficher le BottomBar
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.route in listOf(
            Destination.OPERATEUR.name,
            Destination.CAISSE.name,
            Destination.RAPPORT.name,

            )
    } == true
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { data ->
                CustomSnackbar(data) // ← ton composable custom
            }
        },
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
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(),
                                selectedIconColor = MaterialTheme.colorScheme.background,
                                selectedTextColor = MaterialTheme.colorScheme.background

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
                                        targetState = item,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioHighBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    ) { it ->
                                        Icon(
                                            painter = painterResource(it.selectedIcon),
                                            contentDescription = null,

                                            )
                                    }
                                } else {
                                    Crossfade(targetState = item) {
                                        Icon(
                                            painter = painterResource(it.unSelectedIcon),
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
            navController = navController,
            snackbarHostState = snackbarHostState
        )

    }
}
