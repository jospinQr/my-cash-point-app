package org.megamind.mycashpoint.ui.screen.caisse


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.ConfirmDialog
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.toMontant
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoldeScreen(modifier: Modifier = Modifier, viewModel: SoldeViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val soldes by viewModel.soldes.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect { soldeUiEvent ->

            when (val uiEvent = soldeUiEvent) {
                is SoldeUiEvent.SoldeError -> {
                    Toast.makeText(context, uiEvent.errorMessage, Toast.LENGTH_LONG).show()
                }

                SoldeUiEvent.SoldeSaved -> {
                    Toast.makeText(context, "Enregistrement réussit", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    SoldeScreenContent(
        uiState = uiState,
        soldes = soldes,
        onBottomSheetShown = viewModel::onBottomSheetShown,
        onBottomSheetHide = viewModel::onBottomSheetHide,
        onOperateurMenuExpanded = viewModel::onOperateurMenuExpanded,
        onOperateurMenuDismiss = viewModel::onOperateurMenuDismiss,
        onSelectedOperateurChange = viewModel::onOperateurChange,
        onSelectedDeviseChange = viewModel::onDeviseChange,
        onSaveClick = viewModel::onSaveClick,
        onSoldeInitialChange = viewModel::onSoldeChange,
        onSeuilChange = viewModel::onSeuilChange,
        onSelectedTypeSoldeChange = viewModel::onSoldeTypeChange,
        onConfirmDialogShown = viewModel::onConfirmDialogShown,
        onConfirmDialogDismiss = viewModel::onConfirmDialogDismiss
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoldeScreenContent(

    uiState: SoldeUiState,
    soldes: Solde,
    onBottomSheetShown: () -> Unit,
    onBottomSheetHide: () -> Unit,
    onOperateurMenuExpanded: () -> Unit,
    onOperateurMenuDismiss: () -> Unit,
    onSelectedOperateurChange: (Operateur) -> Unit,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
    onSoldeInitialChange: (String) -> Unit,
    onSeuilChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onSelectedTypeSoldeChange: (SoldeType) -> Unit,
    onConfirmDialogShown: () -> Unit,
    onConfirmDialogDismiss: () -> Unit


) {


    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                "Soldes ${soldes.devise.name}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        })
    }, floatingActionButton = {

        FloatingActionButton(onClick = { onBottomSheetShown() }) {

            Icon(imageVector = Icons.Default.Add, contentDescription = null)

        }
    }) { innerPadding ->


        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 2.dp, vertical = 4.dp)
        ) {


            Column {

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    operateurs.forEachIndexed { index, operateur ->

                        ElevatedFilterChip(
                            elevation = FilterChipDefaults.elevatedFilterChipElevation(
                                elevation = 8.dp
                            ),
                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary

                            ),
                            selected = uiState.selectedOperateur == operateur,
                            onClick = {
                                onSelectedOperateurChange(operateur)
                            },
                            label = {
                                Text(operateur.name)
                            },
                            leadingIcon = {
                                Image(

                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(8.dp)
                                        .clip(CircleShape),
                                    painter = painterResource(operateur.logo),
                                    contentDescription = null
                                )
                            }

                        )

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)

                    )
                    {

                        SoldeType.entries.forEachIndexed { index, soldeType ->

                            SegmentedButton(
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = MaterialTheme.colorScheme.primary,
                                    activeBorderColor = MaterialTheme.colorScheme.primary,
                                    activeContentColor = MaterialTheme.colorScheme.onPrimary


                                ),

                                label = {
                                    Text(
                                        soldeType.name,
                                        color = if (uiState.selecteSoldeType == soldeType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                onClick = {

                                    onSelectedTypeSoldeChange(soldeType)
                                },
                                selected = uiState.selecteSoldeType == soldeType,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = Constants.Devise.entries.size
                                ),

                                )


                        }


                    }
                }

                Box {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text("Devise ")
                        Constants.Devise.entries.forEachIndexed { index, devise ->

                            RadioButton(
                                selected = uiState.selectedDevise == devise,
                                onClick = {
                                    onSelectedDeviseChange(devise)
                                }
                            )
                            Text(devise.name, fontWeight = FontWeight.Bold)
                        }
                    }

                }

                Box(
                    modifier = Modifier

                        .padding(horizontal = 12.dp)
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = .8.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(14.dp)


                    ) {

                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    painter = painterResource(uiState.selectedOperateur.logo),
                                    contentDescription = null
                                )

                                Column {
                                    Text(
                                        "${uiState.selectedOperateur.name} ${soldes.soldeType.name.lowercase()} ${soldes.devise.name}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row {
                                        Text(
                                            "Dernier mis à jour ${Constants.formatTimestamp(soldes.dernierMiseAJour)}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                        )

                                    }
                                }

                            }

                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Solde")
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    Modifier.background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                ) {
                                    Text(
                                        "${soldes.montant.toMontant()} ${soldes.devise.symbole}",
                                        modifier = Modifier.padding(6.dp),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row {
                                Text("Seuil d'alerte")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${soldes.seuilAlerte ?: "0"} ${soldes.devise.symbole}",
                                    color = Color.Red
                                )
                            }


                        }


                    }


                }

            }


        }

    }


    if (uiState.isBottomSheetShown) {

        ModalBottomSheet(
            onDismissRequest = { onBottomSheetHide() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

            ) {

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Initialiser le sole  ")
                    AnimatedContent(uiState.selectedOperateur) {
                        Text(it.name)
                    }
                    AnimatedContent(uiState.selectedDevise) {
                        Text(it.name)
                    }
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Column {

                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier
                                .fillMaxWidth()

                        )
                        {

                            SoldeType.entries.forEachIndexed { index, soldeType ->

                                SegmentedButton(
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.primary,
                                        activeBorderColor = MaterialTheme.colorScheme.primary,
                                        activeContentColor = MaterialTheme.colorScheme.onPrimary


                                    ),

                                    label = {
                                        Text(soldeType.name)
                                    },
                                    onClick = {

                                        onSelectedTypeSoldeChange(soldeType)
                                    },
                                    selected = uiState.selecteSoldeType == soldeType,
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = Constants.Devise.entries.size
                                    ),

                                    )


                            }


                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text("Devise ")
                            Constants.Devise.entries.forEachIndexed { index, devise ->

                                RadioButton(
                                    selected = uiState.selectedDevise == devise,
                                    onClick = {
                                        onSelectedDeviseChange(devise)
                                    }
                                )
                                Text(devise.name, fontWeight = FontWeight.Bold)
                            }
                        }
                        CustomOutlinedTextField(
                            modifier = Modifier.clickable {
                                onOperateurMenuExpanded()
                            },
                            enabled = false,
                            value = uiState.selectedOperateur.name,
                            onValueChange = {

                            },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    painter = painterResource(
                                        uiState.selectedOperateur.logo
                                    ),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {

                                IconButton(onClick = { onOperateurMenuExpanded() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = uiState.isOperateurExpanded,
                            onDismissRequest = { onOperateurMenuDismiss() }

                        ) {

                            operateurs.forEach {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(CircleShape),
                                                painter = painterResource(it.logo),
                                                contentDescription = null
                                            )
                                            Text(it.name)

                                        }

                                    }, onClick = {
                                        onSelectedOperateurChange(it)
                                        onOperateurMenuDismiss()
                                    }
                                )
                            }
                        }
                    }

                    CustomOutlinedTextField(
                        value = uiState.solde,
                        onValueChange = {
                            onSoldeInitialChange(it)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AttachMoney,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        },
                        label = "Solde initial",
                        keyboardType = KeyboardType.Decimal,
                        isError = uiState.isSoldeError,
                        errorMessage = "Le solde initial doit être supérieur à 0"
                    )
                    CustomOutlinedTextField(
                        value = uiState.seuilAlert ?: "0",
                        onValueChange = {
                            onSeuilChange(it)
                        },
                        keyboardType = KeyboardType.Decimal,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AddAlert,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        },
                        label = "Sueil d'alert"
                    )
                    CustomerButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onConfirmDialogShown()
                        },

                        ) {
                        Text("Enregister")
                    }

                }
            }
        }
    }
    if (uiState.isLoading) {
        LoadinDialog()
    }

    ConfirmDialog(


        visible = uiState.isConfirmDialogShown,
        message = "Etes-vous sûr(e) de vouloir enregistrer ce solde ?",
        onConfirm = {
            onSaveClick()
            onBottomSheetHide()
            onConfirmDialogDismiss()
        },
        onDismiss = { onConfirmDialogDismiss() }
    )


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SoldeScreenPreview() {


    MyCashPointTheme {
        // Créez des données mockées
        val mockUiState = SoldeUiState(
            selectedOperateur = operateurs.first(),
            selectedDevise = Constants.Devise.USD, // ou votre devise par défaut
            solde = "1000",
            seuilAlert = "100",
            isBottomSheetShown = false,
            isOperateurExpanded = false,
            isLoading = false,
            selecteSoldeType = SoldeType.PHYSIQUE // Ajustez selon votre enum
        )

        val mockSoldes = Solde(
            montant = BigDecimal(1000),
            dernierMiseAJour = System.currentTimeMillis(),
            seuilAlerte = 10.0,
            devise = Constants.Devise.USD,
            soldeType = SoldeType.PHYSIQUE,

            )

        // Appelez directement CaisseScreenContent (pas CaisseScreen)
        SoldeScreenContent(
            uiState = mockUiState,
            soldes = mockSoldes,
            onBottomSheetShown = {},
            onBottomSheetHide = {},
            onOperateurMenuExpanded = {},
            onOperateurMenuDismiss = {},
            onSelectedOperateurChange = {},
            onSelectedDeviseChange = {},
            onSoldeInitialChange = {},
            onSeuilChange = {},
            onSaveClick = {},
            onSelectedTypeSoldeChange = {},
            onConfirmDialogShown = {},
            onConfirmDialogDismiss = {}
        )
    }
}
