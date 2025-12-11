package org.megamind.mycashpoint.ui.screen.main


import android.content.res.Configuration
import android.net.http.SslCertificate.saveState
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.ui.component.CustomSnackbar
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.navigation.MyNavHost


@Composable
fun MyCashPointApp(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    viewModel: MainViewModel = koinViewModel()
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isAdmin = uiState.userRole == Role.ADMIN


    // Routes qui doivent afficher la BottomBar AGENT
    val agentBottomBarRoutes = listOf(
        Destination.OPERATEUR.name,
        Destination.CAISSE.name,
        Destination.RAPPORT.name
    )

// Routes qui doivent afficher la BottomBar ADMIN
    val adminBottomBarRoutes = listOf(
        Destination.DASHBOARD.name,
        Destination.ADMIN_REPPORT.name,
        Destination.SETTINGS.name
    )


    val currentRoute = currentDestination?.route

    val showAgentBottomBar = currentRoute in agentBottomBarRoutes
    val showAdminBottomBar = currentRoute in adminBottomBarRoutes

    val isCompact =
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    val isMedium = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM
    val isExpanded = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED







    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.imePadding(),
                hostState = snackbarHostState
            ) { data ->
                CustomSnackbar(data) //
            }
        },
        bottomBar = {

            when {
                showAgentBottomBar -> AgentBottomBar(navController, currentDestination)
                showAdminBottomBar -> {
                    if (isCompact) {
                        AdminBottomBar(navController, currentDestination)
                    }
                }
            }
        },


        ) { innerPadding ->


        if (isExpanded && isAdmin) {

            PermanentDrawer(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController,
                currentDestination = currentDestination,
                windowSizeClass = windowSizeClass,
                snackbarHostState = snackbarHostState,

                )


        } else if (isMedium && isAdmin) {
            NavigationRailBar(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                navController = navController,
                currentDestination = currentDestination,
                windowSizeClass = windowSizeClass,
                snackbarHostState = snackbarHostState,
            )
        } else {


            MyNavHost(
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                navController = navController,
                snackbarHostState = snackbarHostState,
                windowSizeClass = windowSizeClass,

                )


        }
    }
}


@Composable
fun AgentBottomBar(navController: NavController, currentDestination: NavDestination?) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .06f)
    ) {
        agentNavBarItem.forEach { item ->

            val selected = currentDestination?.route == item.route

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = .6f)
                ),

                selected = selected,
                onClick = {
                    safeNavigate(
                        navController = navController,
                        currentRoute = currentDestination?.route,
                        targetRoute = item.route
                    )
                },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (selected) item.selectedIcon else item.unSelectedIcon
                        ),
                        contentDescription = null
                    )
                }
            )
        }
    }
}


@Composable
fun AdminBottomBar(navController: NavController, currentDestination: NavDestination?) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = .06f),
    ) {
        adminNavBarItem.forEach { item ->

            val selected = currentDestination?.route == item.route

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = .6f),
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                ),

                selected = selected,
                onClick = {
                    safeNavigate(
                        navController = navController,
                        currentRoute = currentDestination?.route,
                        targetRoute = item.route
                    )
                },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        painter = painterResource(if (selected) item.selectedIcon else item.unSelectedIcon),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationRailBar(
    modifier: Modifier,
    navController: NavHostController,
    currentDestination: NavDestination?,
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
) {
    Row {
        NavigationRail {

            adminNavBarItem.forEach { item ->

                val selected = currentDestination?.route == item.route
                NavigationRailItem(
                    selected = selected,
                    onClick = {
                        safeNavigate(
                            navController = navController,
                            currentRoute = currentDestination?.route,
                            targetRoute = item.route
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) item.selectedIcon else item.unSelectedIcon
                            ),
                            contentDescription = null
                        )
                    },
                    label = { Text(item.title) },
                    alwaysShowLabel = false
                )
            }
        }
        MyNavHost(
            modifier = modifier.weight(1f),
            navController = navController,
            snackbarHostState = snackbarHostState,
            windowSizeClass = windowSizeClass,

            )
    }
}


@Composable
fun PermanentDrawer(
    modifier: Modifier,
    navController: NavHostController,
    currentDestination: NavDestination?,
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,

    ) {

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(drawerTonalElevation = 4.dp, modifier = Modifier.width(220.dp)) {
                adminNavBarItem.forEach { item ->
                    val selected = currentDestination?.route == item.route
                    val bgColor by animateColorAsState(
                        targetValue = if (selected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else
                            Color.Transparent
                    )
                    val iconColor by animateColorAsState(
                        targetValue = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    // Animation taille icône
                    val iconSize by animateDpAsState(
                        targetValue = if (selected) 26.dp else 22.dp
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                painter = painterResource(
                                    if (selected) item.selectedIcon else item.unSelectedIcon
                                ),
                                tint = iconColor,
                                modifier = Modifier.size(iconSize),
                                contentDescription = null
                            )
                        },
                        label = { Text(item.title, color = iconColor) },
                        selected = currentDestination?.route == item.route,
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = bgColor,
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        onClick = {
                            safeNavigate(
                                navController = navController,
                                currentRoute = currentDestination?.route,
                                targetRoute = item.route
                            )
                        }

                    )

                }

            }
        }
    ) {
        MyNavHost(
            modifier = modifier,
            navController = navController,
            snackbarHostState = snackbarHostState,
            windowSizeClass = windowSizeClass,

            )

    }
}


fun safeNavigate(navController: NavController, currentRoute: String?, targetRoute: String) {
    if (currentRoute == targetRoute) return

    navController.navigate(targetRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

