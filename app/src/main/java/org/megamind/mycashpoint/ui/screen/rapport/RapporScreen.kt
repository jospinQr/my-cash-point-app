package org.megamind.mycashpoint.ui.screen.rapport

import android.content.Context
import android.print.PrintManager
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.component.ConfirmDialog
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SnackbarType
import org.megamind.mycashpoint.ui.component.Table
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.MyPrintDocumentAdapter
import java.math.BigDecimal


@Composable
fun RapportScreen(
    modifier: Modifier = Modifier,
    viewModel: RapportViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToAllTransactions: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val transactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val context = LocalContext.current


    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect {

            when (it) {
                is RapportUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = it.errorMessage,
                            type = SnackbarType.ERROR
                        )
                    )

                }

                is RapportUiEvent.ShowSuccesMessage -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = it.succesMessage,
                            type = SnackbarType.SUCCESS
                        )
                    )
                }

            }
        }
    }

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
        onDeleteConfirmationDismiss = viewModel::onDeleteTransactionCancel,
        onTransactionEdit = viewModel::onEditTransactionRequest,
        onTransactionPrint = { transaction ->
            val printManager =
                context.getSystemService(Context.PRINT_SERVICE) as PrintManager
            val jobName = "${context.getString(R.string.app_name)} Reçu"
            val data = mapOf(
                "numero" to transaction.transactionCode,
                "date" to Constants.formatTimestamp(transaction.horodatage),
                "motif" to transaction.type.name,
                "montant" to transaction.montant.toString(),
                "devise" to transaction.devise.symbole,
                "nom" to transaction.nomClient.orEmpty(),
                "agent" to transaction.creePar.toString()
            )
            printManager.print(
                jobName,
                MyPrintDocumentAdapter(context, jobName, data),
                null
            )
        },
        onSendOneTrasactToServer = viewModel::onSendOneTransactToServer,
        onEditSheetDismiss = viewModel::onEditSheetDismiss,
        onEditMontantChange = viewModel::onEditMontantChange,
        onEditNomClientChange = viewModel::onEditNomClientChange,
        onEditTelephoneClientChange = viewModel::onEditTelephoneClientChange,
        onEditNomBeneficiaireChange = viewModel::onEditNomBeneficiaireChange,
        onEditTelephoneBeneficiaireChange = viewModel::onEditTelephoneBeneficiaireChange,
        onEditNoteChange = viewModel::onEditNoteChange,
        onEditDeviseChange = viewModel::onEditDeviseChange,
        onEditTypeChange = viewModel::onEditTypeChange,
        onEditSubmit = viewModel::onEditTransactionSubmit,
        onSyncTransaction = viewModel::onSyncTransaction,
        onSyncSolde = viewModel::onSyncSolde,
        onActionMenuDismiss = viewModel::onActionMenuDismiss,
        onActionMenuVisible = viewModel::onActionMenuVisibile,
        onSyncTransactionDialogShown = viewModel::onSyncTransactConfirmDialog,
        onSyncTransactDialoDismiss = viewModel::onSyncTransactConfirmDialogDismiss,
        onSyncSoldeDialogShown = viewModel::onSyncSoldeConfirmDialog,
        onSyncSoldeDialogDismiss = viewModel::onSyncSoldeConfirmDialogDismiss,
        onNavigateToAllTransactions = onNavigateToAllTransactions
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RapportScreenContent(
    uiState: RapportUiState,
    transactions: List<Transaction>,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
    onSelectedOperateurChange: (Operateur) -> Unit,
    onSearchClick: () -> Unit,
    onSearchBarDismiss: () -> Unit = {},
    onSearchValueChange: (String) -> Unit = {},
    onTransactionClick: (Transaction) -> Unit = {},
    onTransactionDialogDismiss: () -> Unit = {},
    onTransactionDelete: (Transaction) -> Unit = {},
    onDeleteConfirmationConfirm: () -> Unit = {},
    onDeleteConfirmationDismiss: () -> Unit = {},
    onTransactionEdit: (Transaction) -> Unit = {},
    onTransactionPrint: (Transaction) -> Unit = {},
    onEditSheetDismiss: () -> Unit = {},
    onEditMontantChange: (String) -> Unit = {},
    onEditNomClientChange: (String) -> Unit = {},
    onEditTelephoneClientChange: (String) -> Unit = {},
    onEditNomBeneficiaireChange: (String) -> Unit = {},
    onEditTelephoneBeneficiaireChange: (String) -> Unit = {},
    onEditNoteChange: (String) -> Unit = {},
    onEditDeviseChange: (Constants.Devise) -> Unit = {},
    onEditTypeChange: (TransactionType) -> Unit = {},
    onEditSubmit: () -> Unit = {},
    onSendOneTrasactToServer: () -> Unit = {},
    onSyncTransaction: () -> Unit = {},
    onSyncSolde: () -> Unit = {},
    onActionMenuVisible: () -> Unit,
    onActionMenuDismiss: () -> Unit,
    onSyncTransactionDialogShown: () -> Unit,
    onSyncTransactDialoDismiss: () -> Unit = {},
    onSyncSoldeDialogShown: () -> Unit,
    onSyncSoldeDialogDismiss: () -> Unit = {},
    onNavigateToAllTransactions: () -> Unit = {}

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


                    IconButton(onClick = { onActionMenuVisible() }) {

                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)

                    }

                    DropdownMenu(
                        expanded = uiState.isActionMenuVisible,
                        onDismissRequest = { onActionMenuDismiss() }

                    ) {


                        actionMenus.forEachIndexed { index, actionMenu ->

                            DropdownMenuItem(
                                text = { Text(actionMenu.text) },
                                onClick = {

                                    when (index) {

                                        0 -> {
                                            onSyncTransactionDialogShown()

                                        }

                                        1 -> {
                                            onSyncSoldeDialogShown()
                                        }

                                        2 -> {

                                        }
                                    }
                                }
                            )
                        }


                    }


                }
            )
        }) { innerPadding ->


        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        )
        {


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

                if (transactions.isNotEmpty()) {
                    TransactionTable(
                        transactions = transactions,
                        onRowClick = onTransactionClick
                    )

                } else {

                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Aucune transaction non synchronisé")
                        Button(onClick = { onNavigateToAllTransactions() }) { Text("Transactions synchronisées") }
                    }
                }

            }


        }
        if (uiState.isLoading) {
            LoadinDialog()
        }
        if (uiState.isTransactionDialogVisible && uiState.selectedTransaction != null) {
            TransactionDetailDialog(
                transaction = uiState.selectedTransaction,
                onDismissRequest = onTransactionDialogDismiss,
                onDeleteClick = onTransactionDelete,
                onEditClick = onTransactionEdit,
                onSyncClick = onSendOneTrasactToServer,
                onPrintClick = onTransactionPrint
            )
        }

        if (uiState.isDeleteConfirmationVisible && uiState.selectedTransaction != null) {
            DeleteTransactionConfirmationDialog(
                transaction = uiState.selectedTransaction,
                onConfirm = onDeleteConfirmationConfirm,
                onDismiss = onDeleteConfirmationDismiss
            )
        }




        ConfirmDialog(
            visible = uiState.isSyncTransactConformDialogShown,
            title = "Vous êtes sur le point d'envoyer les transactions sur le serveur.",
            onDismiss = { onSyncTransactDialoDismiss() },
            onConfirm = { onSyncTransaction() }
        )
        ConfirmDialog(
            visible = uiState.isSyncSoldeConformDialogShown,
            title = "Vous êtes sur le point d'envoyer les soldes sur le serveur.",
            onDismiss = { onSyncSoldeDialogDismiss() },
            onConfirm = { onSyncSolde() }
        )


        if (uiState.isEditSheetVisible) {
            TransactionEditBottomSheet(
                uiState = uiState,
                onDismiss = onEditSheetDismiss,
                onTypeChange = onEditTypeChange,
                onDeviseChange = onEditDeviseChange,
                onMontantChange = onEditMontantChange,
                onNomClientChange = onEditNomClientChange,
                onTelephoneClientChange = onEditTelephoneClientChange,
                onNomBeneficiaireChange = onEditNomBeneficiaireChange,
                onTelephoneBeneficiaireChange = onEditTelephoneBeneficiaireChange,
                onNoteChange = onEditNoteChange,
                onSubmit = onEditSubmit
            )
        }


    }
}


@Composable
fun TransactionTable(
    transactions: List<Transaction>,
    onRowClick: (Transaction) -> Unit = {}
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


@Composable
private fun TransactionDetailDialog(
    transaction: Transaction,
    onDismissRequest: () -> Unit,
    onDeleteClick: (Transaction) -> Unit,
    onEditClick: (Transaction) -> Unit,
    onSyncClick: () -> Unit,
    onPrintClick: (Transaction) -> Unit,

    ) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                TextButton(

                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSyncClick() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.secondary.copy(.09f)
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Sync, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Envoyer au serveur")
                }
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onPrintClick(transaction) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.DarkGray,
                        containerColor = Color.Green.copy(.09f)
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Print, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Imprimer")
                }


                TextButton(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth()) {

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
                Text("Devise: ${transaction.devise.name}")
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
    transaction: Transaction,
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
            TextButton(
                onClick = onConfirm, colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun TransactionEditBottomSheet(
    uiState: RapportUiState,
    onDismiss: () -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onDeviseChange: (Constants.Devise) -> Unit,
    onMontantChange: (String) -> Unit,
    onNomClientChange: (String) -> Unit,
    onTelephoneClientChange: (String) -> Unit,
    onNomBeneficiaireChange: (String) -> Unit,
    onTelephoneBeneficiaireChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Modifier la transaction ${uiState.selectedTransaction?.transactionCode.orEmpty()}",
                style = MaterialTheme.typography.titleMedium
            )

            val transactionTypes = remember { TransactionType.entries.toList() }
            var isTypeMenuExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = isTypeMenuExpanded,
                onExpandedChange = { isTypeMenuExpanded = !isTypeMenuExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = uiState.editSelectedType.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type de transaction") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypeMenuExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = isTypeMenuExpanded,
                    onDismissRequest = { isTypeMenuExpanded = false }
                ) {
                    transactionTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                onTypeChange(type)
                                isTypeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                Constants.Devise.entries.forEachIndexed { index, devise ->
                    SegmentedButton(
                        label = { Text(devise.name) },
                        onClick = { onDeviseChange(devise) },
                        selected = uiState.editSelectedDevise == devise,
                        shape = SegmentedButtonDefaults.itemShape(
                            index,
                            Constants.Devise.entries.size
                        ),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            CustomOutlinedTextField(
                value = uiState.editMontant,
                onValueChange = onMontantChange,
                label = "Montant",
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
                isError = uiState.isEditMontantError,
                errorMessage = "Montant invalide",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.AttachMoney,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            CustomOutlinedTextField(
                value = uiState.editNomClient,
                onValueChange = onNomClientChange,
                label = "Nom client",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                isError = uiState.isEditNomClientError,
                errorMessage = "Champs obligatoire",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            CustomOutlinedTextField(
                value = uiState.editTelephoneClient,
                onValueChange = onTelephoneClientChange,
                label = "Téléphone client",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                isError = uiState.isEditTelephoneClientError,
                errorMessage = "Champs obligatoire",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            if (uiState.editSelectedType == TransactionType.DEPOT) {
                CustomOutlinedTextField(
                    value = uiState.editNomBeneficiaire,
                    onValueChange = onNomBeneficiaireChange,
                    label = "Bénéficiaire",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    isError = uiState.isEditNomBeneficiaireError,
                    errorMessage = "Champs obligatoire",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                CustomOutlinedTextField(
                    value = uiState.editTelephoneBeneficiaire,
                    onValueChange = onTelephoneBeneficiaireChange,
                    label = "Téléphone bénéficiaire",
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next,
                    isError = uiState.isEditTelephoneBeneficiaireError,
                    errorMessage = "Champs obligatoire",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            CustomOutlinedTextField(
                value = uiState.editNote,
                onValueChange = onNoteChange,
                label = "Note",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                singleLine = false,
                maxLines = 3,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )

            if (uiState.editErrorMessage.isNotBlank()) {
                Text(
                    text = uiState.editErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            CustomerButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enable = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Enregistrer les modifications")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RapportScreenContentPreview() {
    MaterialTheme {
        RapportScreenContent(
            uiState = RapportUiState(
                selectedDevise = Constants.Devise.USD,
                selectedOperateur = operateurs[0], // Orange Money
                isSearchBarShown = false,
                searchValue = "",
                isTransactionDialogVisible = false,
                isDeleteConfirmationVisible = false,
                isEditSheetVisible = false,
                selectedTransaction = null,
                isLoading = false,
                editSelectedType = TransactionType.DEPOT,
                editSelectedDevise = Constants.Devise.USD,
                editMontant = "",
                editNomClient = "",
                editTelephoneClient = "",
                editNomBeneficiaire = "",
                editTelephoneBeneficiaire = "",
                editNote = "",
                isEditMontantError = false,
                isEditNomClientError = false,
                isEditTelephoneClientError = false,
                isEditNomBeneficiaireError = false,
                isEditTelephoneBeneficiaireError = false,
                editErrorMessage = ""
            ),
            transactions = listOf(
                Transaction(
                    id = 1,
                    transactionCode = "TXN001",
                    type = TransactionType.DEPOT,
                    montant = BigDecimal(5000.0),
                    soldeAvant = BigDecimal(10000.0),
                    soldeApres = BigDecimal(15000.0),
                    devise = Constants.Devise.USD,
                    nomClient = "Jean Mukendi",
                    numClient = "+243 812 345 678",
                    nomBeneficaire = "Marie Kalala",
                    numBeneficaire = "+243 998 765 432",
                    note = "Transfert urgent",
                    horodatage = System.currentTimeMillis() - 3600000,
                    creePar = 1L,
                    idOperateur = 1,
                    reference = "",

                    ),
                Transaction(
                    id = 2,
                    transactionCode = "TXN002",
                    type = TransactionType.RETRAIT,
                    montant = BigDecimal(2000.0),
                    soldeAvant = BigDecimal(15000.0),
                    soldeApres = BigDecimal(13000.0),
                    devise = Constants.Devise.CDF,
                    nomClient = "Pierre Kabila",
                    numClient = "+243 823 456 789",
                    nomBeneficaire = null,
                    numBeneficaire = null,
                    note = null,
                    horodatage = System.currentTimeMillis() - 7200000,
                    creePar = 1L,
                    idOperateur = 1,

                    reference = "",

                    ),
                Transaction(
                    id = 3,
                    transactionCode = "TXN003",
                    type = TransactionType.DEPOT,
                    montant = BigDecimal(10000.0),
                    soldeAvant = BigDecimal(13000.0),
                    soldeApres = BigDecimal(23000.0),
                    devise = Constants.Devise.USD,
                    nomClient = "Alice Mbuyi",
                    numClient = "+243 897 654 321",
                    nomBeneficaire = "Bob Tshisekedi",
                    numBeneficaire = "+243 876 543 210",
                    note = "Paiement facture",
                    horodatage = System.currentTimeMillis() - 10800000,
                    creePar = 1,
                    idOperateur = 1,
                    reference = "",

                    )
            ),
            onSelectedDeviseChange = {},
            onSelectedOperateurChange = {},
            onSearchClick = {},
            onSearchBarDismiss = {},
            onSearchValueChange = {},
            onTransactionClick = {},
            onTransactionDialogDismiss = {},
            onTransactionDelete = {},
            onDeleteConfirmationConfirm = {},
            onDeleteConfirmationDismiss = {},
            onTransactionEdit = {},
            onTransactionPrint = {},
            onEditSheetDismiss = {},
            onEditMontantChange = {},
            onEditNomClientChange = {},
            onEditTelephoneClientChange = {},
            onEditNomBeneficiaireChange = {},
            onEditTelephoneBeneficiaireChange = {},
            onEditNoteChange = {},
            onEditDeviseChange = {},
            onEditTypeChange = {},
            onEditSubmit = {},
            onSendOneTrasactToServer = {},
            onActionMenuDismiss = {},
            onActionMenuVisible = {},
            onSyncTransactionDialogShown = {},
            onSyncTransactDialoDismiss = {},
            onSyncSoldeDialogShown = {},
            onSyncSoldeDialogDismiss = {}
        )
    }
}
