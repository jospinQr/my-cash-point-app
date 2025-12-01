package org.megamind.mycashpoint.ui.screen.auth

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SnackbarType
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme


/**
 * Fonction composable pour l'écran de connexion.
 *
 * Cette fonction affiche l'interface utilisateur pour l'authentification de l'utilisateur,
 * permettant aux utilisateurs de se connecter avec leur email et mot de passe ou via Google Sign-In.
 * Elle gère les mises à jour de l'état de l'interface utilisateur, les interactions de l'utilisateur
 * et la navigation en fonction des résultats de l'authentification.
 *
 * @param modifier [Modifier] optionnel pour le thème et la mise en page.
 * @param viewModel Le [LoginViewModel] responsable de la gestion de l'état et de la logique de l'écran.
 *                  Par défaut, une instance injectée par Koin.
 * @param navigateToMainScreen Une fonction lambda à appeler lorsque l'utilisateur se connecte avec succès,
 *                       déclenchant la navigation vers l'écran d'accueil.
 * @param windowSizeClass Fournit des informations sur la taille de la fenêtre, utilisées pour les mises en page adaptatives.
 */
@Composable
fun LoginInScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    navigateToAgentHome: () -> Unit,
    navigateToAdminHome: () -> Unit,
    windowSizeClass: WindowSizeClass,
    snackbarHostate: SnackbarHostState

) {

    val uiState by viewModel.uiState.collectAsState()

    SignInEventHandler(
        viewModel = viewModel,
        navigateToAgentHome = { navigateToAgentHome() },
        navigateToAdminHome = { navigateToAdminHome() },
        snackbarHostState = snackbarHostate,
    )


    SignInScreenContent(
        uiState = uiState,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onSignIn = { viewModel.onSignIn() },
        onPasswordVisibilityChange = { viewModel.onPasswordVisibilityChange() },
        windowSizeClass = windowSizeClass,


        )
}

@Composable
fun SignInScreenContent(
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    windowSizeClass: WindowSizeClass,


    ) {
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Scaffold { innerPadding ->
        AdaptiveSignInLayout(
            modifier = Modifier.padding(innerPadding),
            isCompact = isCompact,
            uiState = uiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onSignIn = onSignIn,
            onPasswordVisibilityChange = onPasswordVisibilityChange,


            )
    }

    if (uiState.isLoading) {
        LoadinDialog(text = "Connexion...")
    }


}

@Composable
private fun AdaptiveSignInLayout(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,


    ) {
    if (isCompact) {
        Column(
            modifier = modifier

                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            SignInContent(
                uiState = uiState,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onSignIn = onSignIn,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                showLogoAbove = true,


                )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LogoSection(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SignInContent(
                    uiState = uiState,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onSignIn = onSignIn,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,

                    showLogoAbove = false,


                    )
            }
        }
    }
}

@Composable
private fun SignInContent(
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    showLogoAbove: Boolean,


    ) {
    if (showLogoAbove) {
        LogoSection(modifier = Modifier.clickable {})
    }


    AuthTextField(
        value = uiState.email,
        onValueChange = onEmailChange,
        label = "Nom",
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.isEmailError,
        supportText = "Entrer une adresse email valide"
    )

    Spacer(modifier = Modifier.height(4.dp))

    AuthTextField(
        value = uiState.password,

        onValueChange = onPasswordChange,
        label = "Mot de passe",
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (uiState.isPasswordShown) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityIcon(
                isPasswordShown = uiState.isPasswordShown,
                onPasswordVisibilityChange = onPasswordVisibilityChange
            )
        },
        isError = uiState.isPasswordError,
        supportText = "Entrer un mot de passe valide"
    )

    SignInButtonsSection(
        uiState = uiState,
        onSignIn = onSignIn,


        )

    if (uiState.isSendingPasswordResetEmail) {
        LoadinDialog(text = "Envoi en cours...")
    }
}

@Composable
private fun PasswordVisibilityIcon(
    isPasswordShown: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {
    AnimatedContent(targetState = isPasswordShown) { passwordShown ->
        Icon(
            imageVector = if (passwordShown) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = "Password visibility",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .clickable { onPasswordVisibilityChange() }
        )
    }
}


@Composable
private fun SignInButtonsSection(
    uiState: SignInUiState,
    onSignIn: () -> Unit,

    ) {
    CustomerButton(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onSignIn,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        SignInButtonContent(
            isLoading = uiState.isSigningIn,
            loadingText = "Connexion...",
            normalText = "Connexion",
            showProgressAtEnd = uiState.isSigningIn
        )
    }


}


@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(94.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Bienvenue sur MyCashPoint",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }

}

@Composable
private fun SignInButtonContent(
    isLoading: Boolean,
    loadingText: String,
    normalText: String,
    showProgressAtEnd: Boolean = false,
    painter: Painter? = null

) {
    if (isLoading) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(text = loadingText)
            Spacer(Modifier.width(16.dp))
            if (showProgressAtEnd) {
                Spacer(modifier = Modifier.width(8.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }


        }
    } else {
        Text(text = normalText)
        Spacer(Modifier.width(12.dp))

        if (painter != null) {
            Image(
                modifier = Modifier.size(26.dp),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
    }
}


// Composable séparé pour gérer les événements - Plus stable avec Live Edit
@Composable
private fun SignInEventHandler(
    viewModel: LoginViewModel,
    navigateToAgentHome: () -> Unit,
    navigateToAdminHome: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SignInUiEvent.NavigateToAgentHomeScreen -> {
                    navigateToAgentHome()
                }

                is SignInUiEvent.NavigateToAdmintHomeScreen -> {
                    navigateToAdminHome()
                }

                is SignInUiEvent.ShowError -> {

                    snackbarHostState
                        .showSnackbar(
                            visuals = CustomSnackbarVisuals(
                                message = event.message,
                                type = SnackbarType.ERROR
                            )
                        )

                }


            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun LoginScreePreview() {

    val snackbarHostate = remember { SnackbarHostState() }

    MyCashPointTheme {

        LoginInScreen(
            navigateToAgentHome = {},
            navigateToAdminHome = {},
            windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
            snackbarHostate = snackbarHostate

        )

    }
}