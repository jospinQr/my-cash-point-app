package org.megamind.mycashpoint.ui.screen.rapport

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.component.Table
import org.megamind.mycashpoint.utils.Constants

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RapportScreen(modifier: Modifier = Modifier, viewModel: RapportViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val transactions by viewModel.transaction.collectAsStateWithLifecycle()

    RapportScreenContent(
        uiState = uiState,
        transactions = transactions,
        onSelectedDeviseChange = viewModel::onSelectedDeviseChange,
        onSelectedOperateurChange = viewModel::onSelectedOperateurChange,
        onSearchClick = viewModel::onSearchClick,
        onSearchBarDismiss = viewModel::onSearchBarDismiss,
        onSearchValueChange = viewModel::onSearchValueChange,
        onTransactionClick = viewModel::onTransactionClick,
        onTransactionDialogDismiss = viewModel::onTransactionDialogDismiss,
        onTransactionDelete = viewModel::onDeleteTransactionRequest,
        onDeleteConfirmationConfirm = viewModel::onDeleteTransactionConfirm,
        onDeleteConfirmationDismiss = viewModel::onDeleteTransactionCancel
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RapportScreenContent(
    uiState: RapportUiState,
    transactions: List<TransactionEntity>,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
    onSelectedOperateurChange: (Operateur) -> Unit,
    onSearchClick: () -> Unit,
    onSearchBarDismiss: () -> Unit = {},
    onSearchValueChange: (String) -> Unit = {},
    onTransactionClick: (TransactionEntity) -> Unit = {},
    onTransactionDialogDismiss: () -> Unit = {},
    onTransactionDelete: (TransactionEntity) -> Unit = {},
    onDeleteConfirmationConfirm: () -> Unit = {},
    onDeleteConfirmationDismiss: () -> Unit = {},
    onTransactionEdit: (TransactionEntity) -> Unit = {},
    onTransactionSync: (TransactionEntity) -> Unit = {}
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Rapport ${uiState.selectedOperateur.name}") },
                navigationIcon = {
                    Image(

                        modifier = Modifier
                            .size(62.dp)
                            .padding(8.dp)
                            .clip(CircleShape),
                        painter = painterResource(uiState.selectedOperateur.logo),
                        contentDescription = null
                    )
                },
                actions = {

                    IconButton(onClick = { onSearchClick() }) {

                        Icon(imageVector = Icons.Rounded.Search, contentDescription = null)

                    }


                    IconButton(onClick = {}) {

                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)

                    }


                }
            )
        }) { innerPadding ->


        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                AnimatedVisibility(
                    visible = uiState.isSearchBarShown,
                    enter = slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    )
                ) {

                    AuthTextField(
                        value = uiState.searchValue,
                        onValueChange = { onSearchValueChange(it) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { onSearchBarDismiss() }) {

                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = null
                                )


                            }
                        }

                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    operateurs.forEachIndexed { index, operateur ->

                        ElevatedFilterChip(
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
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()

                )
                {

                    Constants.Devise.entries.forEachIndexed { index, devise ->

                        SegmentedButton(
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeBorderColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary


                            ),

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
                Spacer(modifier = Modifier.height(16.dp))


                TransactionTable(
                    transactions = transactions,
                    onRowClick = onTransactionClick
                )

                if (uiState.isTransactionDialogVisible && uiState.selectedTransaction != null) {
                    TransactionDetailDialog(
                        transaction = uiState.selectedTransaction,
                        onDismissRequest = onTransactionDialogDismiss,
                        onDeleteClick = onTransactionDelete,
                        onEditClick = onTransactionEdit,
                        onSyncClick = onTransactionSync
                    )
                }

                if (uiState.isDeleteConfirmationVisible && uiState.selectedTransaction != null) {
                    DeleteTransactionConfirmationDialog(
                        transaction = uiState.selectedTransaction,
                        onConfirm = onDeleteConfirmationConfirm,
                        onDismiss = onDeleteConfirmationDismiss
                    )
                }


            }


        }


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionTable(
    transactions: List<TransactionEntity>,
    onRowClick: (TransactionEntity) -> Unit = {}
) {


    val headers = listOf("Type", "Montant", "Avant", "Apres", "Client", "Telephone", "date")

    Table(
        columnCount = headers.size,
        headers = headers,
        items = transactions,
        onRowClick = onRowClick,
    ) { columnIndex, _, item ->

        when (columnIndex) {
            0 -> Text(item.type.name)
            1 -> Text(item.montant.toString())
            2 -> Text(item.soldeAvant.toString())
            3 -> Text(item.soldeApres.toString())
            4 -> Text(item.nomClient ?: "")
            5 -> Text(item.numClient ?: "")
            6 -> Text(Constants.formatTimestamp(item.horodatage))
            else -> {}
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TransactionDetailDialog(
    transaction: TransactionEntity,
    onDismissRequest: () -> Unit,
    onDeleteClick: (TransactionEntity) -> Unit,
    onEditClick: (TransactionEntity) -> Unit,
    onSyncClick: (TransactionEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                TextButton(
                    onClick = { onSyncClick(transaction) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.secondary.copy(.09f)
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Sync, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Synchroniser")
                }
                TextButton(
                    onClick = { onEditClick(transaction) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.tertiary,
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(.09f)
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Modifier")
                }
                TextButton(
                    onClick = { onDeleteClick(transaction) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primary.copy(.09f)
                    )
                ) {

                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Supprimer")
                }
                TextButton(
                    onClick = { onDeleteClick(transaction) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.DarkGray,
                        containerColor = Color.Green.copy(.09f)
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Print, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imprimer")
                }


                TextButton(onClick = onDismissRequest) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Fermer")
                }
            }
        },
        title = { Text("Détails de la transaction") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Code: ${transaction.transactionCode}")
                Text("Type: ${transaction.type.name}")
                Text("Montant: ${transaction.montant}")
                Text("Solde avant: ${transaction.soldeAvant ?: "-"}")
                Text("Solde après: ${transaction.soldeApres ?: "-"}")
                transaction.nomClient?.takeIf { it.isNotBlank() }?.let {
                    Text("Client: $it")
                }
                transaction.numClient?.takeIf { it.isNotBlank() }?.let {
                    Text("Téléphone client: $it")
                }
                transaction.nomBeneficaire?.takeIf { it.isNotBlank() }?.let {
                    Text("Bénéficiaire: $it")
                }
                transaction.numBeneficaire?.takeIf { it.isNotBlank() }?.let {
                    Text("Téléphone bénéficiaire: $it")
                }
                Text("Devise: ${transaction.device.name}")
                Text("Date: ${Constants.formatTimestamp(transaction.horodatage)}")
                transaction.note?.takeIf { it.isNotBlank() }?.let {
                    Text("Note: $it")
                }
            }
        }
    )
}

@Composable
private fun DeleteTransactionConfirmationDialog(
    transaction: TransactionEntity,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmer la suppression") },
        text = {
            Text(
                "Voulez-vous vraiment supprimer la transaction ${transaction.transactionCode} ? Cette action mettra également à jour les soldes."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )) {
                Text("Supprimer")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}