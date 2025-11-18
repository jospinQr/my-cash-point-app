package org.megamind.mycashpoint.ui.screen

import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.ui.component.AnimatedTextByLetter
import org.megamind.mycashpoint.ui.screen.splash.SplashUiEvent
import org.megamind.mycashpoint.ui.screen.splash.SplashViewModel
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme


@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = koinViewModel(),
    navigateToLoginScreen: () -> Unit = {},
    navigateToHomeScreen: () -> Unit = {}
) {


    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect {
            when (it) {
                SplashUiEvent.NavigateToHome -> {
                    navigateToHomeScreen()
                }

                SplashUiEvent.NavigateToLogin -> {
                    navigateToLoginScreen()
                }
            }
        }
    }



    Scaffold { innerPadding ->


        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {


            Icon(
                modifier = Modifier.size(202.dp),
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedTextByLetter(

                    text = stringResource(R.string.app_name),
                    startAnimation = true,

                    )

                Text(
                    "Par MegaMind-DRC",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                )
            }


        }
    }


}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showBackground = true)
fun SplashScreenPrevie() {

    MyCashPointTheme {

        SplashScreen(navigateToLoginScreen = {})
    }
}
