package org.megamind.mycashpoint.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass

import org.megamind.mycashpoint.ui.screen.agence.AgenceScreen
import org.megamind.mycashpoint.ui.screen.splash.SplashScreen
import org.megamind.mycashpoint.ui.screen.admin.rapport.AdminRepportScreen
import org.megamind.mycashpoint.ui.screen.admin.dash_board.DashBoardScreen
import org.megamind.mycashpoint.ui.screen.admin.SettingsScreen

import org.megamind.mycashpoint.ui.screen.auth.LoginInScreen
import org.megamind.mycashpoint.ui.screen.auth.RegisterScreen
import org.megamind.mycashpoint.ui.screen.solde.SoldeScreen

import org.megamind.mycashpoint.ui.screen.rapport.RapportScreen
import org.megamind.mycashpoint.ui.screen.operateur.OperateurScreen
import org.megamind.mycashpoint.ui.screen.transaction.AllTransactionScreen
import org.megamind.mycashpoint.ui.screen.transaction.TransactionScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startDestination: String = Destination.SPLASH.name,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,

    ) {


    SharedTransitionLayout {
        NavHost(
            startDestination = startDestination, navController = navController, modifier = modifier
        ) {
            composable(route = Destination.SPLASH.name) {
                SplashScreen(
                    modifier = Modifier,
                    navigateToAdminHomeScreen = {
                        navController.navigate(Destination.DASHBOARD.name) {
                            popUpTo(Destination.SPLASH.name) {
                                inclusive = true
                            }
                        }
                    },
                    navigateToAgentHomeScreen = {
                        navController.navigate(Destination.OPERATEUR.name) {
                            popUpTo(Destination.SPLASH.name) {
                                inclusive = true
                            }
                        }
                    }, navigateToLoginScreen = {
                        navController.navigate(Destination.LOGIN.name) {
                            popUpTo(Destination.SPLASH.name) {
                                inclusive = true
                            }
                        }
                    }
                )

            }

            composable(route = Destination.LOGIN.name) {
                LoginInScreen(
                    snackbarHostate = snackbarHostState,
                    windowSizeClass = windowSizeClass,
                    navigateToAdminHome = {
                        navController.navigate(Destination.DASHBOARD.name) {
                            popUpTo(Destination.LOGIN.name) {
                                inclusive = true
                            }

                        }
                    },
                    navigateToAgentHome = {
                        navController.navigate(Destination.OPERATEUR.name) {
                            popUpTo(Destination.LOGIN.name) {
                                inclusive = true
                            }

                        }
                    }

                )
            }

            composable(route = Destination.REGISTER.name) {
                RegisterScreen(onNavigateToSignUp = {
                    navController.navigate(Destination.AGENCE.name)
                }, windowSizeClass = windowSizeClass, navigateToHome = {})

            }




            composable(route = Destination.OPERATEUR.name) {

                OperateurScreen(

                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    navigateToSignIn = {
                        navController.navigate(Destination.LOGIN.name) {
                            popUpTo(0) {
                                inclusive = true
                            }

                        }
                    },
                    snackBarHostState = snackbarHostState,
                    navigateToTransactionScreen = {
                        navController.navigate(Destination.TRANSACTION.name,)
                    })
            }

            composable(route = Destination.CAISSE.name) {

                SoldeScreen(snackbarHostState = snackbarHostState)
            }

            composable(route = Destination.RAPPORT.name) {

                RapportScreen(
                    onNavigateToAllTransactions = {
                        navController.navigate(Destination.SYNCTRSACT.name)
                    },
                    snackbarHostState = snackbarHostState
                )
            }

            composable(route = Destination.TRANSACTION.name) {

                TransactionScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )

            }
            composable(route = Destination.AGENCE.name) {
                AgenceScreen(snackbarHostState = snackbarHostState)
            }


            composable(route = Destination.DASHBOARD.name) {
                DashBoardScreen(
                    navigateToLoginScreen = {
                        navController.navigate(Destination.LOGIN.name){
                            popUpTo(0){
                                inclusive=true
                            }
                        }
                    },

                    navigateToCreateAgence = {
                        navController.navigate(Destination.AGENCE.name)
                    },
                    navigateToCreateAgent = {
                        navController.navigate(Destination.REGISTER.name)
                    },
                    snackbarHostState = snackbarHostState
                )
            }

            composable(route = Destination.ADMIN_REPPORT.name) {

                AdminRepportScreen()
            }

            composable(route = Destination.SETTINGS.name) {

                SettingsScreen()
            }

            composable(route = Destination.SYNCTRSACT.name) {

                AllTransactionScreen(modifier = Modifier, onBack = {navController.popBackStack() })




            }


        }

    }
}