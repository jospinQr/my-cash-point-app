package org.megamind.mycashpoint.ui.screen.auth


import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.CustomerTextButton
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    windowSizeClass: WindowSizeClass,

    ) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    RegisterEventHandler(
        viewModel = viewModel,
        context = context,
        navigateToHome = navigateToHome
    )


    RegisterScreenContent(
        uiState = uiState,
        onEmailChange = { viewModel.onEmailChange(it) },
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onSignIn = { viewModel.onRegister() },
        onNameChange = { viewModel.onNameChange(it) },
        onPasswordRepeatChange = { viewModel.onPasswordRepeatChange(it) },
        onPasswordVisibilityChange = { viewModel.onPasswordVisibilityChange() },
        windowSizeClass = windowSizeClass,
        onNavigateToSignUp = onNavigateToSignUp,


        )

}

@Composable
fun RegisterScreenContent(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    windowSizeClass: WindowSizeClass,
    onNavigateToSignUp: () -> Unit

) {
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Scaffold { innerPadding ->
        AdaptiveRegisterLayout(
            modifier = Modifier.padding(innerPadding),
            isCompact = isCompact,
            uiState = uiState,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onSignIn = onSignIn,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            onNavigateToSignUp = onNavigateToSignUp,
            onNameChange = onNameChange,
            onPasswordRepeatChange = onPasswordRepeatChange,


            )
    }


}

@Composable
private fun AdaptiveRegisterLayout(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onNavigateToSignUp: () -> Unit,


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

            IconSection(modifier = Modifier)
            RegisterContent(
                uiState = uiState,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onNameChange = onNameChange,
                onPasswordRepeatChange = onPasswordRepeatChange,
                onSignIn = onSignIn,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                onNavigateToSignUp = onNavigateToSignUp

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
            IconSection(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                RegisterContent(
                    uiState = uiState,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onNameChange = onNameChange,
                    onPasswordRepeatChange = onPasswordRepeatChange,
                    onSignIn = onSignIn,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    onNavigateToSignUp = onNavigateToSignUp,


                    )
            }
        }
    }
}

@Composable
private fun RegisterContent(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onNavigateToSignUp: () -> Unit,


    ) {


    AuthTextField(
        value = uiState.userName,
        onValueChange = onNameChange,
        label = "Nom",
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.isNameError,
        supportText = "Champ obligatoire"
    )
    Spacer(modifier = Modifier.height(4.dp))
    AuthTextField(
        value = uiState.email,
        onValueChange = onEmailChange,
        label = "Email",
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
        supportText = "Entrer un mot de passe valide(8 caractères minimum)"
    )
    Spacer(modifier = Modifier.height(4.dp))
    AuthTextField(
        value = uiState.passWordRepeat,

        onValueChange = onPasswordRepeatChange,
        label = "Répéter le mot de passe",
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (uiState.isPasswordShown) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityIcon(
                isPasswordShown = uiState.isPasswordShown,
                onPasswordVisibilityChange = onPasswordVisibilityChange
            )
        },
        isError = uiState.isPasswordRepError,
        supportText = "Les mots de passe ne correspondent pas"
    )





    RegisterButtonsSection(
        uiState = uiState,
        onSignIn = onSignIn,
        onNavigateToSignUp = onNavigateToSignUp
    )


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
private fun RegisterButtonsSection(
    uiState: RegisterUiState,
    onSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    CustomerButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSignIn,
        contentColor = MaterialTheme.colorScheme.background
    ) {
        RegisterButtonContent(
            isLoading = uiState.isRegisting,
            loadingText = "Connexion...",
            normalText = "Connexion",
            showProgressAtEnd = uiState.isRegisting
        )
    }

    Spacer(modifier = Modifier.height(16.dp))


    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {


        Text(
            modifier = Modifier,
            text = "Mot de passe oublié ?",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(2.dp))
        CustomerTextButton(
            modifier = Modifier,
            onClick = {

                onNavigateToSignUp()

            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Text(
                text = "S'inscrire",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }

    }


}


@Composable
fun IconSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(94.dp),
            imageVector = Icons.Default.People,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Inscrivez vous pour continuer avec  MyCashPoint",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )
    }

}

@Composable
private fun RegisterButtonContent(
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
private fun RegisterEventHandler(
    viewModel: RegisterViewModel,
    context: Context,
    navigateToHome: () -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {

                RegisterUiEvent.NavigateToHome -> {}
                is RegisterUiEvent.ShowError -> {}
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreePreview() {


    MyCashPointTheme {

        RegisterScreen(
            navigateToHome = {},
            onNavigateToSignUp = {},
            windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        )

    }
}