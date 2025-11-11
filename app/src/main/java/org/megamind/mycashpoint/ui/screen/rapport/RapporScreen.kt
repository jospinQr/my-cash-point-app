package org.megamind.mycashpoint.ui.screen.rapport

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
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
        onSelectedOperateurChange = viewModel::onSelectedOperateurChange
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
                }
            )
        }) { innerPadding ->


        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {


            Column {

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


                    TransactionTable(transactions = transactions)
               

            }


        }


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionTable(transactions: List<TransactionEntity>) {


    val headers = listOf("Type", "Montant", "Avant","Apres", "Client", "Telephone", "date")

    Table(

        columnCount = headers.size,
        headers = headers,
        items = transactions,

        ) { columnIndex, _, item ->

        when (columnIndex) {
            0 -> Text(item.type.name)
            1 -> Text(item.montant.toString())
            2 -> Text(item.soldeAvant.toString())
            3-> Text(item.soldeApres.toString())
            4 -> Text(item.nomClient ?: "")
            5 -> Text(item.numClient ?: "")
            6 -> Text(Constants.formatTimestamp(item.horodatage))
            else -> {}
        }

    }


}