package org.megamind.mycashpoint.ui.screen.auth


import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.ui.screen.agence.AgenceViewModel
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.CustomerTextButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
    agenceViewModel: AgenceViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    windowSizeClass: WindowSizeClass,



    ) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val agences by agenceViewModel.agences.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect { uiEvent ->

            when (uiEvent) {
                RegisterUiEvent.NavigateToHome -> {

                }

                is RegisterUiEvent.ShowError -> {
                    Toast.makeText(context, uiEvent.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    RegisterScreenContent(
        uiState = uiState,
        onPasswordChange = { viewModel.onPasswordChange(it) },
        onSignIn = { viewModel.onRegister() },
        onNameChange = { viewModel.onNameChange(it) },
        onPasswordRepeatChange = { viewModel.onPasswordRepeatChange(it) },
        onPasswordVisibilityChange = { viewModel.onPasswordVisibilityChange() },
        windowSizeClass = windowSizeClass,
        onNavigateToSignUp = onNavigateToSignUp,
        onAgenceMenuExpanded = { viewModel.onAgenceMenuExpanded() },
        onAgenceMenuDismiss = { viewModel.onAgenceMenuDismiss() },
        onSelectedAgenceChange = { viewModel.onAgenceChange(it) },
        agences = agences,
        onUserRoleChange = viewModel::onUserRoleChange,
    )

}

@Composable
fun RegisterScreenContent(
    uiState: RegisterUiState,

    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    windowSizeClass: WindowSizeClass,
    onNavigateToSignUp: () -> Unit,
    onAgenceMenuExpanded: () -> Unit,
    onAgenceMenuDismiss: () -> Unit,
    onSelectedAgenceChange: (Agence) -> Unit,
    agences: List<Agence>,
    onUserRoleChange: (Role) -> Unit


) {
    val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Scaffold { innerPadding ->
        AdaptiveRegisterLayout(
            modifier = Modifier.padding(innerPadding),
            isCompact = isCompact,
            uiState = uiState,
            onPasswordChange = onPasswordChange,
            onSignIn = onSignIn,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            onNavigateToSignUp = onNavigateToSignUp,
            onNameChange = onNameChange,
            onPasswordRepeatChange = onPasswordRepeatChange,
            onAgenceMenuExpanded = onAgenceMenuExpanded,
            onAgenceMenuDismiss = onAgenceMenuDismiss,
            onSelectedAgenceChange = onSelectedAgenceChange,
            agences = agences,
            onUserRoleChange = onUserRoleChange


        )
    }

    if (uiState.isLoading) {
        LoadinDialog()
    }

}

@Composable
private fun AdaptiveRegisterLayout(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    uiState: RegisterUiState,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onAgenceMenuExpanded: () -> Unit,
    onAgenceMenuDismiss: () -> Unit,
    onSelectedAgenceChange: (Agence) -> Unit,
    agences: List<Agence>,
    onUserRoleChange: (Role) -> Unit


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
                onPasswordChange = onPasswordChange,
                onNameChange = onNameChange,
                onPasswordRepeatChange = onPasswordRepeatChange,
                onSignIn = onSignIn,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                onNavigateToSignUp = onNavigateToSignUp,
                onAgenceMenuExpanded = onAgenceMenuExpanded,
                onAgenceMenuDismiss = onAgenceMenuDismiss,
                onSelectedAgenceChange = onSelectedAgenceChange,
                agences = agences,
                onUserRoleChange = onUserRoleChange

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
                    onPasswordChange = onPasswordChange,
                    onNameChange = onNameChange,
                    onPasswordRepeatChange = onPasswordRepeatChange,
                    onSignIn = onSignIn,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    onNavigateToSignUp = onNavigateToSignUp,
                    onAgenceMenuExpanded = onAgenceMenuExpanded,
                    onAgenceMenuDismiss = onAgenceMenuDismiss,
                    onSelectedAgenceChange = onSelectedAgenceChange,
                    agences = agences,


                    )
            }
        }
    }
}

@Composable
private fun RegisterContent(
    uiState: RegisterUiState,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordRepeatChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onAgenceMenuExpanded: () -> Unit,
    onAgenceMenuDismiss: () -> Unit,
    onSelectedAgenceChange: (Agence) -> Unit,
    agences: List<Agence>,
    onUserRoleChange: (Role) -> Unit = {}


) {


    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        Role.entries.forEachIndexed { index, role ->

            Text(
                text = role.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            RadioButton(
                selected = uiState.selecteRole == role,
                onClick = {
                    onUserRoleChange(role)
                }
            )

        }

    }
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
        value = uiState.password,

        onValueChange = onPasswordChange,
        label = "Mot de passe",
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityIcon(
                isPasswordShown = uiState.isPasswordVisible,
                onPasswordVisibilityChange = onPasswordVisibilityChange
            )
        },
        isError = uiState.isPasswordError,
        supportText = "Entrer un mot de passe valide(8 caractères minimum)"
    )
    AuthTextField(
        value = uiState.passWordRepeat,

        onValueChange = onPasswordRepeatChange,
        label = "Repéter le mot de passe",
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordVisibilityIcon(
                isPasswordShown = uiState.isPasswordVisible,
                onPasswordVisibilityChange = onPasswordVisibilityChange
            )
        },
        isError = uiState.isPasswordError,
        supportText = "Entrer un mot de passe valide(8 caractères minimum)"
    )
    Spacer(modifier = Modifier.height(4.dp))
    Column {
        AuthTextField(
            label = "Agence",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAgenceMenuExpanded()
                },
            enabled = false,
            value = uiState.agence,
            onValueChange = {

            },

            trailingIcon = {

                IconButton(onClick = { onAgenceMenuExpanded() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        DropdownMenu(
            expanded = uiState.isAgenceExpanded,
            onDismissRequest = { onAgenceMenuDismiss() }

        )
        {

            agences.forEach {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(it.designation)

                        }

                    }, onClick = {
                        onSelectedAgenceChange(it)
                        onAgenceMenuDismiss()
                    }
                )
            }
        }
    }



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
            "Veillez inscrir un agent ou un adminisrateur",
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

        RegisterScreenContent(
            uiState = RegisterUiState(),
            onPasswordChange = {},
            onNameChange = {},
            onPasswordRepeatChange = {},
            onSignIn = {},
            onPasswordVisibilityChange = {},
            onNavigateToSignUp = {},
            onAgenceMenuExpanded = {},
            onAgenceMenuDismiss = {},
            onSelectedAgenceChange = {},
            agences = emptyList(),
            windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
            onUserRoleChange = {}


        )

    }
}