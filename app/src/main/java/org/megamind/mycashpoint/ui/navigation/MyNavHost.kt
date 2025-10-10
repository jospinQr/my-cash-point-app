package org.megamind.mycashpoint.ui.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import org.megamind.mycashpoint.ui.screen.SplashScreen
import org.megamind.mycashpoint.ui.screen.auth.LoginInScreen
import org.megamind.mycashpoint.ui.screen.auth.RegisterScreen
import org.megamind.mycashpoint.ui.screen.main.MainScreen


@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destination.SPLASH.name,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {


    NavHost(
        startDestination = startDestination,
        navController = navController,
        modifier = modifier
    ) {
        composable(route = Destination.SPLASH.name) {
            SplashScreen(modifier = Modifier, navigateToLoginScreen = {
                navController.navigate(Destination.LOGIN.name) {
                    popUpTo(Destination.SPLASH.name) {
                        inclusive = true
                    }
                }
            })

        }

        composable(route = Destination.LOGIN.name) {
            LoginInScreen(
                onNavigateToSignUp = {
                    navController.navigate(Destination.REGISTER.name)
                },
                windowSizeClass = windowSizeClass,
                navigateToMainScreen = {

                    navController.navigate(Destination.MAIN.name) {
                        popUpTo(Destination.LOGIN.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Destination.REGISTER.name) {
            RegisterScreen(
                onNavigateToSignUp = {},
                windowSizeClass = windowSizeClass,
                navigateToHome = {}
            )

        }


        composable(route = Destination.MAIN.name) {
            MainScreen(
                windowSizeClass = windowSizeClass
            )
        }
    }


}