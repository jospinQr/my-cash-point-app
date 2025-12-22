package org.megamind.mycashpoint.ui.screen.admin.etablissement

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SnackbarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtablissementScreen(
    modifier: Modifier = Modifier,
    viewModel: EtablissementViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Informations de l'établissement",
                    style = MaterialTheme.typography.titleMedium
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Détails de l'entreprise",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                CustomOutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = "Nom de l'établissement",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.address,
                    onValueChange = viewModel::onAddressChange,
                    label = "Adresse",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.contact,
                    onValueChange = viewModel::onContactChange,
                    label = "Contact",
                    modifier = Modifier.fillMaxWidth()
                )

                CustomOutlinedTextField(
                    value = uiState.rccm,
                    onValueChange = viewModel::onRccmChange,
                    label = "RCCM",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = viewModel::onUpdate,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Mettre à jour les informations")
                }
            }

            if (uiState.isLoading) {
                LoadinDialog(text = "Chargement...")
            }

            if (uiState.error != null) {
                Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
