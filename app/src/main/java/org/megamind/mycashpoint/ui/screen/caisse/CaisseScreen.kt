package org.megamind.mycashpoint.ui.screen.caisse


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.utils.Constants

@Composable
fun CaisseScreen(modifier: Modifier = Modifier, viewModel: CaisseViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val soldes by viewModel.soldes.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect { caisseUiEvent ->

            when (val uiEvent = caisseUiEvent) {
                is CaisseUiEvent.CaisseError -> {
                    Toast.makeText(context, uiEvent.errorMessage, Toast.LENGTH_LONG).show()
                }

                CaisseUiEvent.CaisseSaved -> {
                    Toast.makeText(context, "Enregistrement réussit", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    CaisseScreenContent(
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
        onSeuilChange = viewModel::onSeuilChange
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaisseScreenContent(

    uiState: CaisseUiState,
    soldes: List<SoldeEntity>,
    onBottomSheetShown: () -> Unit,
    onBottomSheetHide: () -> Unit,
    onOperateurMenuExpanded: () -> Unit,
    onOperateurMenuDismiss: () -> Unit,
    onSelectedOperateurChange: (Operateur) -> Unit,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
    onSoldeInitialChange: (String) -> Unit,
    onSeuilChange: (String) -> Unit,
    onSaveClick: () -> Unit,


    ) {


    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                "Soldes ${uiState.selectedDevise.name}",
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
        ) {


            if (soldes.isEmpty()) {
                Text("Aucune donnée", modifier = Modifier.align(Alignment.Center))
                return@Scaffold
            }

            LazyColumn {
                item {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                    {

                        Constants.Devise.entries.forEachIndexed { index, devise ->

                            SegmentedButton(
                                colors = SegmentedButtonDefaults.colors(),

                                label = {
                                    Text(devise.name)
                                },
                                onClick = {

                                    onSelectedDeviseChange(devise)
                                },
                                selected = uiState.selectedDevise == devise,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = Constants.Devise.entries.size
                                ),

                                )


                        }


                    }
                }

                itemsIndexed(soldes) { index, solde ->

                    val operateur = operateurs.find { operateur ->
                        operateur.id == solde.idOperateur &&
                                uiState.selectedDevise == solde.devise


                    }
                    if (operateur != null) {
                        val isLast = index == soldes.lastIndex
                        val isFirst = index == 0
                        ListItem(
                            modifier = Modifier.border(
                                width = .08.dp,
                                Color.Gray,
                                shape = RoundedCornerShape(
                                    topStart = if (isFirst) 42.dp else 0.dp,
                                    topEnd = if (isFirst) 42.dp else 0.dp,
                                    bottomEnd = if (isLast) 42.dp else 0.dp,
                                    bottomStart = if (isLast) 42.dp else 0.dp,
                                )
                            ),
                            leadingContent = {
                                Image(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                14.dp
                                            ),
                                        ),
                                    painter = painterResource(
                                        operateur.logo
                                    ),
                                    contentDescription = null

                                )
                            },
                            headlineContent = { Text(operateur.name) },
                            supportingContent = { Text("${solde.montant} ${solde.devise.name} ") },
                            trailingContent = {
                                Text(Constants.formatTimestamp(solde.dernierMiseAJour))
                            }
                        )
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
                    AnimatedContent(uiState.selectedoperateur) {
                        Text(it?.name ?: "")
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
                            value = uiState.selectedoperateur?.name ?: "",
                            onValueChange = {

                            },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    painter = painterResource(
                                        uiState.selectedoperateur?.logo ?: R.drawable.logo
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
                        label = "Solde initial"
                    )
                    CustomOutlinedTextField(
                        value = uiState.seuilAlert ?: "0",
                        onValueChange = {
                            onSeuilChange(it)
                        },
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
                            onSaveClick()
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


}


