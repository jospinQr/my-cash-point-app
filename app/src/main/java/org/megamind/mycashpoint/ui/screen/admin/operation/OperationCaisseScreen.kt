package org.megamind.mycashpoint.ui.screen.admin.operation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Note
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.OperationCaisseType
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationCaisseScreen(
    navController: NavController, viewModel: OperationCaisseViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is OperationCaisseEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is OperationCaisseEvent.ShowSuccess -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                "Opérations Caisse",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Retour")
            }
        })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // SECTION 1: CONTEXTE (Où j'agis ?)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Store,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Contexte de l'opération",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        // Agence Selector
                        Text(
                            "Agence",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        TextDropdown(
                            modifier = Modifier.fillMaxWidth(),
                            items = uiState.agences,
                            selectedItem = uiState.selectedAgence,
                            onItemSelected = { viewModel.onSelectedAgenceChange(it) },
                            expanded = uiState.isAgenceExpanded,
                            onExpandedChange = { viewModel.onAgenceDropdownExpandChange(it) },
                            getText = { it.designation }
                        )

                        // Operator Selector
                        Text(
                            "Opérateur",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        TextDropdown(
                            modifier = Modifier.fillMaxWidth(),
                            items = operateurs,
                            selectedItem = uiState.selectedOperateur,
                            onItemSelected = { viewModel.onOperateurSelected(it) },
                            expanded = uiState.isOperateurExpanded,
                            onExpandedChange = { viewModel.onOperateurDropdownExpandChange(it) },
                            getText = { it.name }
                        )

                        // SoldeType Selector
                        Text(
                            "Type de Solde à impacter",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        TextDropdown(
                            modifier = Modifier.fillMaxWidth(),
                            items = SoldeType.entries,
                            selectedItem = uiState.selectedSoldeType,
                            onItemSelected = { viewModel.onSelectedSoldeTypeChange(it) },
                            expanded = uiState.isSoldeTypeExpanded,
                            onExpandedChange = { viewModel.onSoldeTypeDropdownExpandChange(it) },
                            getText = { it.name }
                        )
                    }
                }

                // SECTION 2: CONFIGURATION (Quoi faire ?)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Type & Devise",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Type Selector
                        Text(
                            "Nature de l'opération",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height(120.dp)
                        ) {
                            items(OperationCaisseType.entries) { type ->
                                FilterChip(
                                    selected = uiState.selectedOperationCaisseType == type,
                                    onClick = { viewModel.onTypeSelected(type) },
                                    label = {
                                        Text(
                                            type.name.replace("_", " "),
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        // Currency Selector
                        Text(
                            "Devise de la transaction",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Constants.Devise.entries.forEachIndexed { index, devise ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = Constants.Devise.entries.size
                                    ),
                                    onClick = { viewModel.onCurrencySelected(devise) },
                                    selected = uiState.selectedCurrency == devise
                                ) {
                                    Text(devise.name)
                                }
                            }
                        }
                    }
                }

                // SECTION 3: DETAILS (Combien et pourquoi ?)
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Détails financiers",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Form Inputs
                        CustomOutlinedTextField(
                            value = uiState.amount,
                            onValueChange = { viewModel.onAmountChange(it) },
                            label = "Montant",
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                            leadingIcon = {
                                Icon(Icons.Rounded.AttachMoney, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        CustomOutlinedTextField(
                            value = uiState.motif,
                            onValueChange = { viewModel.onMotifChange(it) },
                            label = "Motif / Justificatif",
                            imeAction = ImeAction.Done,
                            leadingIcon = {
                                Icon(Icons.Rounded.Note, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                CustomerButton(
                    onClick = { viewModel.onSubmit() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enable = !uiState.isLoading
                ) {
                    Text(if (uiState.isLoading) "Traitement en cours..." else "Valider l'opération")
                }
            }

            if (uiState.isLoading) {
                LoadinDialog()
            }
        }
    }
}
