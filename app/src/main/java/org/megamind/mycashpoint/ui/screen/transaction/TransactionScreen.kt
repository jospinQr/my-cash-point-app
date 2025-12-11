package org.megamind.mycashpoint.ui.screen.transaction

import android.content.Context
import android.os.Build
import android.print.PrintManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.ui.component.ConfirmDialog
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SnackbarType
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.screen.operateur.OperateurUiState
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.MyPrintDocumentAdapter




@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: TransactionViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {

    val context = LocalContext.current
    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect {

            when (it) {
                is TransactionUiEvent.ShowErrorMessage -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = it.errorMessage,
                            type = SnackbarType.ERROR
                        )
                    )

                }

                is TransactionUiEvent.ShowSuccessMessage -> {
                    snackbarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            message = it.successMessage,
                            type = SnackbarType.SUCCESS
                        )
                    )
                }

                is TransactionUiEvent.ReprintReceipt -> {

                    val transaction = it.transaction
                    val userName = it.user
                    val printManager =
                        context.getSystemService(Context.PRINT_SERVICE) as PrintManager
                    val jobName = "${context.getString(R.string.app_name)} Doc"
                    printManager.print(
                        jobName,
                        MyPrintDocumentAdapter(
                            context,
                            "Fiche de stock",

                            mapOf(
                                "numero" to transaction.transactionCode,
                                "date" to Constants.formatTimestamp(transaction.horodatage),
                                "motif" to transaction.type.name,
                                "montant" to "${transaction.montant}",
                                "devise" to transaction.devise.symbole,
                                "nom" to transaction.nomClient.toString(),
                                "agent" to userName,

                                )
                        ),
                        null
                    )
                }
            }
        }
    }
    val uiState by viewModel.uiSate.collectAsStateWithLifecycle()


    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Destination.OPERATEUR.name)
    }


    val operateurViewModel: OperateurViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
    val operateurUiState by operateurViewModel.uiState.collectAsStateWithLifecycle()

    TransactionScreenContent(
        uiState = uiState,
        sharedTransitionScope = sharedTransitionScope,
        operateurUiState = operateurUiState,
        animatedVisibilityScope = animatedVisibilityScope,
        onSelectedDevise = viewModel::onDeviseSelected,
        onFormVisble = viewModel::onFormVisble,
        onFormInvisble = viewModel::onFormInvisble,
        onTypeSelected = viewModel::onTypeSelected,
        onMontantChange = viewModel::onMontantChange,
        onNomClientChange = viewModel::onNomClientChange,
        onTelephClientChange = viewModel::onTelephClientChange,
        onNomBeneFChange = viewModel::onNomBenefChange,
        onTelephBenefChange = viewModel::onTelephBenefChange,
        onNoteChange = viewModel::onNoteChange,
        onSave = viewModel::onSaveClick,
        onConfirmDialogDismiss = viewModel::onConfirmDialogDismiss,
        onConfirmDialogShown = viewModel::onConfirmDialogShown,
        onCommissionChange = viewModel::onCommissionChange
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreenContent(
    uiState: TransactionUiState,
    sharedTransitionScope: SharedTransitionScope,
    operateurUiState: OperateurUiState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSelectedDevise: (Constants.Devise) -> Unit,
    onFormVisble: () -> Unit,
    onFormInvisble: () -> Unit,
    onTypeSelected: (TransactionType) -> Unit,
    onMontantChange: (String) -> Unit,
    onNomClientChange: (String) -> Unit,
    onTelephClientChange: (String) -> Unit,
    onNomBeneFChange: (String) -> Unit,
    onTelephBenefChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: (Long) -> Unit,
    onConfirmDialogDismiss: () -> Unit,
    onConfirmDialogShown: () -> Unit,
    onCommissionChange : (String)->Unit

    ) {

    val selectedOperateur = operateurUiState.selectedOperateur


    with(sharedTransitionScope) {

        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        selectedOperateur?.name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(
                                    key = operateurUiState.selectedOperateur?.id ?: ""
                                ),
                                animatedVisibilityScope
                            )
                    )
                },
                navigationIcon = {
                    Image(

                        modifier = Modifier
                            .size(42.dp)
                            .sharedElement(
                                rememberSharedContentState(
                                    operateurUiState.selectedOperateur?.name ?: ""
                                ), animatedVisibilityScope
                            )
                            .clip(CircleShape),
                        painter = painterResource(selectedOperateur!!.logo),
                        contentDescription = selectedOperateur.name

                    )
                },

                )
        }, floatingActionButton = {

        }) { innerPadding ->


            Box(modifier = Modifier.padding(innerPadding)) {


                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)

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
                                    Text(
                                        devise.name,
                                        color = if (uiState.selectedDevise == devise) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                onClick = {

                                    onSelectedDevise(devise)
                                },
                                selected = uiState.selectedDevise == devise,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = Constants.Devise.entries.size
                                ),

                                )


                        }


                    }
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center

                    ) {

                        LazyVerticalGrid(

                            modifier = Modifier
                                .fillMaxWidth(),
                            columns = GridCells.Fixed(2)
                        ) {


                            itemsIndexed(TransactionType.entries) { index, typTransct ->


                                TransactionTypeButton(
                                    onClick = {

                                        onTypeSelected(typTransct)
                                        onFormVisble()
                                    },
                                    label = typTransct.name,
                                    icon = typTransct.icon
                                )

                            }
                        }


                    }
                }

            }

        }
    }

    if (uiState.isFormVisble) {

        ModalBottomSheet(
            onDismissRequest = { onFormInvisble() },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            ),


            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        " ${uiState.selectedType.name} ${selectedOperateur?.name} en ${uiState.selectedDevise.name}".uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CustomOutlinedTextField(
                        value = uiState.montant,
                        onValueChange = {
                            onMontantChange(it)
                        },
                        label = "Montant",
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                        isError = uiState.isMontantError,
                        errorMessage = "Montant doit être superieur à 0",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.AttachMoney,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )


                    CustomOutlinedTextField(
                        value = uiState.nomClient,
                        onValueChange = {
                            onNomClientChange(it)
                        },
                        label = "Nom client",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = uiState.isNomError,
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
                        value = uiState.telephClient,
                        onValueChange = {
                            onTelephClientChange(it)
                        },
                        label = "Telephone client",
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                        isError = uiState.isTelephClientError,
                        errorMessage = "Champs obligatoire",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    if (uiState.selectedType == TransactionType.DEPOT) {
                        CustomOutlinedTextField(
                            value = uiState.nomBenef,
                            onValueChange = {
                                onNomBeneFChange(it)
                            },
                            label = "Beneficiaire",
                            imeAction = ImeAction.Next,
                            isError = uiState.isNomBenefError,
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

                            value = uiState.telephBenef,
                            onValueChange = {
                                onTelephBenefChange(it)
                            },
                            label = "Telephone Beneficiaire",
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next,
                            isError = uiState.isTelephBenefError,
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
                        value = uiState.commission,
                        onValueChange = {
                            onCommissionChange(it)
                        },
                        label = "Commission en %",
                        imeAction = ImeAction.Done,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Money,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    )
                    CustomOutlinedTextField(
                        value = uiState.note,
                        onValueChange = {
                            onNoteChange(it)
                        },
                        label = "Note",
                        imeAction = ImeAction.Done,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Note,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    )
                    CustomerButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onConfirmDialogShown()
                        }
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
        onDismiss = {
            onConfirmDialogDismiss()

        },
        onConfirm = {
            onSave(selectedOperateur?.id ?: 0)
            onConfirmDialogDismiss()
        },
    )
}

@Composable
fun TransactionTypeButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    onClick: () -> Unit
) {


    Card(
        modifier = Modifier.padding(4.dp),
        border = BorderStroke(width = 0.8f.dp, color = MaterialTheme.colorScheme.primary),
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 22.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Icon(

                painter = painterResource(icon),
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionScreenContentPreview() {
    MaterialTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                TransactionScreenContent(
                    uiState = TransactionUiState(
                        selectedDevise = Constants.Devise.USD,
                        selectedType = TransactionType.DEPOT,
                        montant = "100.00",
                        nomClient = "Jean Dupont",
                        telephClient = "+243 123 456 789",
                        nomBenef = "Marie Dupont",
                        telephBenef = "+243 987 654 321",
                        note = "Transaction test",
                        isFormVisble = false,
                        isMontantError = false,
                        isNomError = false,
                        isTelephClientError = false,
                        isNomBenefError = false,
                        isTelephBenefError = false,
                        isLoading = false,
                        isConfirmDialogShown = false
                    ),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    operateurUiState = OperateurUiState(
                        selectedOperateur = Operateur(
                            id = 1,
                            name = "Orange Money",
                            logo = R.drawable.orange_logo,
                            color = Color.Black// Remplacer par votre logo
                        ),

                        ),
                    animatedVisibilityScope = this,
                    onSelectedDevise = {},
                    onFormVisble = {},
                    onFormInvisble = {},
                    onTypeSelected = {},
                    onMontantChange = {},
                    onNomClientChange = {},
                    onTelephClientChange = {},
                    onNomBeneFChange = {},
                    onTelephBenefChange = {},
                    onNoteChange = {},
                    onSave = {},
                    onConfirmDialogDismiss = {},
                    onConfirmDialogShown = {},
                    onCommissionChange = {}
                )
            }
        }
    }
}

