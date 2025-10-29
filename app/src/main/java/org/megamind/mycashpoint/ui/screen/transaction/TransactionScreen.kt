package org.megamind.mycashpoint.ui.screen.transaction

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold

import androidx.compose.material3.SegmentedButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.screen.operateur.OperateurUiState
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.utils.Constants
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.megamind.mycashpoint.data.data_source.local.entity.TypTransct
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: TransationViewModel = koinViewModel()
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
        onNomClientChange = viewModel::onNomClentChange,
        onTelephClientChange = viewModel::onTelephClientChange,
        onNomBeneFChange = viewModel::onNomBenefChange,
        onTelephBenefChange = viewModel::onTelephBenefChange,
        onNoteChange = viewModel::_onNoteChange,
        onSave = viewModel::onSaveClick
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
    onTypeSelected: (TypTransct) -> Unit,
    onMontantChange: (String) -> Unit,
    onNomClientChange: (String) -> Unit,
    onTelephClientChange: (String) -> Unit,
    onNomBeneFChange: (String) -> Unit,
    onTelephBenefChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: (Int) -> Unit

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
                }
            )
        }) { innerPadding ->


            Box(modifier = Modifier.padding(innerPadding)) {

                Column {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)
                    ) {

                        Constants.Devise.entries.forEachIndexed { index, devise ->

                            SegmentedButton(
                                colors = SegmentedButtonDefaults.colors(),

                                label = {
                                    Text(devise.name)
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TypTransct.entries.forEachIndexed { index, typTransct ->

                                CustomerButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onTypeSelected(typTransct)
                                        onFormVisble()
                                    },
                                    contenairColor = if (index % 2 == 0) MaterialTheme.colorScheme.primary.copy(
                                        0.07f
                                    )
                                    else MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(16.dp),


                                    ) {

                                    Text(
                                        typTransct.name,
                                        color = if (index % 2 == 0) MaterialTheme.colorScheme.primary
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

    if (uiState.isFormVisble) {

        ModalBottomSheet(
            onDismissRequest = { onFormInvisble() }, sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    " ${uiState.selectedType.name} ${selectedOperateur?.name} en ${uiState.selectedDevise.name}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
                    errorMessage = "Montant doit être superieur à 0"
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
                    errorMessage = "Champs obligatoire"
                )
                CustomOutlinedTextField(
                    value = uiState.telephClient,
                    onValueChange = {
                        onNomClientChange(it)
                    },
                    label = "Telephone client",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    isError = uiState.isTelephClientError,
                    errorMessage = "Champs obligatoire"
                )
                if (uiState.selectedType == TypTransct.DEPOT) {
                    CustomOutlinedTextField(
                        value = uiState.nomBenef,
                        onValueChange = {
                            onNomBeneFChange(it)
                        },
                        label = "Beneficiaire",
                        imeAction = ImeAction.Next,
                        isError = uiState.isNomBenefError,
                        errorMessage = "Champs obligatoire"
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
                        errorMessage = "Champs obligatoire"
                    )
                }
                CustomOutlinedTextField(
                    value = uiState.note,
                    onValueChange = {
                        onNoteChange(it)
                    },
                    label = "Note",
                    imeAction = ImeAction.Done
                )
                CustomerButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSave(selectedOperateur!!.id)
                    }
                ) {
                    Text("Enregister")
                }

            }

        }
    }

    if (uiState.isLoading) {
        LoadinDialog()
    }
}


