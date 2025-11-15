package org.megamind.mycashpoint.ui.screen.transaction

import android.content.Context
import android.os.Build
import android.print.PrintManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Note
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Note
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.ui.component.ConfirmDialog

import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.screen.operateur.OperateurUiState
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.MyPrintDocumentAdapter
import org.megamind.mycashpoint.utils.UtilsFonctions
import org.megamind.mycashpoint.utils.decodeJwtPayload


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: TransactionViewModel = koinViewModel()
) {

    val context = LocalContext.current
    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect {

            when (it) {
                is TransactionUiEvent.TransactionError -> {
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()
                }

                TransactionUiEvent.TransactionSaved -> {
                    Toast.makeText(context, "Enregistrement reussit", Toast.LENGTH_LONG).show()
                }

                is TransactionUiEvent.TransactionPrint -> {

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
                                "devise" to transaction.device.symbole,
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
    onSave: (Int) -> Unit,
    onConfirmDialogDismiss: () -> Unit,
    onConfirmDialogShown: () -> Unit,

    ) {

    val selectedOperateur = operateurUiState.selectedOperateur
    val context = LocalContext.current


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
                }
            )
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

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TransactionType.entries.forEachIndexed { index, typTransct ->

                                val isFirst = index == 0
                                val isLast = index == 4
                                CustomerButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onTypeSelected(typTransct)
                                        onFormVisble()
                                    },
                                    contenairColor = if (index % 2 == 0) MaterialTheme.colorScheme.primary.copy(

                                    )
                                    else MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(
                                        topEnd = if (isFirst) 22.dp else 0.dp,
                                        topStart = if (isFirst) 22.dp else 0.dp,
                                        bottomStart = if (isLast) 22.dp else 0.dp,
                                        bottomEnd = if (isLast) 22.dp else 0.dp,
                                    ),


                                    ) {

                                    Row {
                                        Text(
                                            typTransct.name,
                                            color = if (index % 2 == 0) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
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


