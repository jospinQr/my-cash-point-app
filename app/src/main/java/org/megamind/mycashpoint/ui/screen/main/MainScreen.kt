package org.megamind.mycashpoint.ui.screen.main

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun MainScreen(modifier: Modifier = Modifier, windowSizeClass: WindowSizeClass) {


    val isScreenCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT


    if (isScreenCompact) {
        Scaffold(

            bottomBar = {
                NavigationBar() {
                    NavigationBarItem(
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null
                            )
                        })

                }
            }
        ) { innePadding ->


        }
    } else {

        Scaffold { innnerPadding ->

            Row {

                NavigationRail {
                    NavigationRailItem(
                        selected = true,
                        onClick = {},
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null
                            )
                        }

                    )

                }


            }

        }
    }

}






