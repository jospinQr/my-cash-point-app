package org.megamind.mycashpoint.ui.screen.transaction

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.AuthTextField
import org.megamind.mycashpoint.ui.screen.rapport.TransactionTable
import org.megamind.mycashpoint.ui.screen.rapport.actionMenus
import org.megamind.mycashpoint.utils.Constants

@Composable
fun AllTransactionScreen(
    modifier: Modifier, viewModel: AllTransactionViewModel = koinViewModel(), onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredTransactions by viewModel.filteredTransactions.collectAsStateWithLifecycle()

    AllTransactionScreenContent(
        uiState = uiState,
        onSelectedDeviseChange = viewModel::onSelectedDeviseChange,
        onSelectedOperateurChange = viewModel::onSelectedOperatorChange,
        onTransactionClick = viewModel::onTransactionClick,
        onSearchValueChange = viewModel::onSearchValueChange,
        onSearchBarDismiss = viewModel::onSearchBarDismiss,
        transactions = filteredTransactions,
        onSearchClick = viewModel::onSearchClick

    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllTransactionScreenContent(
    uiState: AllTransactUiState,
    transactions: List<Transaction>,
    onSelectedDeviseChange: (Constants.Devise) -> Unit = {},
    onSelectedOperateurChange: (Operateur) -> Unit = {},
    onTransactionClick: (Transaction) -> Unit = {},
    onSearchValueChange: (String) -> Unit = {},
    onSearchBarDismiss: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Scaffold(
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


                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            Column(modifier = Modifier) {

                Box(
                    Modifier
                        .fillMaxSize()

                        .padding(horizontal = 16.dp),
                ) {


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        AnimatedVisibility(
                            visible = uiState.isSearchBarShown, enter = slideInVertically(
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
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = null
                                    )
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
                            modifier = Modifier.fillMaxWidth()

                        ) {

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
                                        index = index, count = Constants.Devise.entries.size
                                    ),

                                    )


                            }


                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (transactions.isNotEmpty()) {
                            TransactionTable(
                                transactions = transactions, onRowClick = onTransactionClick
                            )

                        } else {

                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text("Aucune transaction non synchronisé")
                                Button(onClick = {}) { Text("Transactions synchronisées") }
                            }
                        }

                    }


                }


            }


        }
    }
}