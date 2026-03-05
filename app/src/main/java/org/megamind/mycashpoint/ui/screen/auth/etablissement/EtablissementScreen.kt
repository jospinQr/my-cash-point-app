package org.megamind.mycashpoint.ui.screen.auth.etablissement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.SnackbarType
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtablissementScreen(
    modifier: Modifier = Modifier,
    viewModel: EtablissementViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToUrlScreen: () -> Unit,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                EtablissementUiEvent.OnUpdateSuccess -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = "Informations mises à jour avec succès",
                            type = SnackbarType.SUCCESS
                        )
                    )
                }

                is EtablissementUiEvent.OnError -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = event.message,
                            type = SnackbarType.ERROR
                        )
                    )
                }

                EtablissementUiEvent.OnSaveSuccess -> {
                    navigateToUrlScreen()
                }
            }
        }
    }

    EtablissementScreenContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onAddressChange = viewModel::onAddressChange,
        onContactChange = viewModel::onContactChange,
        onRccmChange = viewModel::onRccmChange,
        onUpdate = viewModel::saveOrUpDate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EtablissementScreenContent(
    uiState: EtablissementUiState,
    onNameChange: (String) -> Unit = {},
    onAddressChange: (String) -> Unit = {},
    onContactChange: (String) -> Unit = {},
    onRccmChange: (String) -> Unit = {},
    onUpdate: () -> Unit = {},


    ) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Decrivez votre établissement",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Text("Merci pour votre inscription !", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Votre accès à MyCashPoint sera activé dès réception de votre URL personnelle Celle-ci vous sera envoyée par SMS ou email dans quelques instants. Vous pourrez ensuite vous connecter facilement à l’application.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )


                CustomOutlinedTextField(
                    value = uiState.name,
                    onValueChange = onNameChange,
                    label = "Nom de l'établissement",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.address,
                    onValueChange = onAddressChange,
                    label = "Adresse",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.contact,
                    onValueChange = onContactChange,
                    label = "Contact",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.rccm,
                    onValueChange = onRccmChange,
                    label = "RCCM",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onUpdate,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 4.dp
                        )
                        return@Button
                    }
                    Text("Continuer")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        "Vous avez déjà un Url pour votre cash point?",
                        style = MaterialTheme.typography.bodySmall,

                        )
                    TextButton(onClick = {}) {
                        Text("Cliquez ici", style = MaterialTheme.typography.bodySmall)
                    }

                }
            }

        }
    }
}


@Composable
@Preview
fun EtablissementScreenPreview() {

    MyCashPointTheme {
        EtablissementScreenContent(
            uiState = EtablissementUiState(isLoading = false, error = null),

            )
    }
}