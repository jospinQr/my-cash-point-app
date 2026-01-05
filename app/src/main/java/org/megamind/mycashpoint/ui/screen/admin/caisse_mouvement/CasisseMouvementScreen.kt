package org.megamind.mycashpoint.ui.screen.admin.caisse_mouvement

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.SoldeMouvement
import org.megamind.mycashpoint.ui.component.Table
import org.megamind.mycashpoint.utils.Constants

@Composable
fun CaisseMouvementScreen(
    modifier: Modifier = Modifier, viewModel: CaisseMouvementViewModel = koinViewModel()
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CaisseMouvementScreenContent(
        uiState = uiState, onSelectePeriodeChange = viewModel::onPeriodeChange
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaisseMouvementScreenContent(
    uiState: AdminTransactionUiState,
    onSelectePeriodeChange: (PeriodeFiltre) -> Unit = {},


    ) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = .06f
                )
            ), title = {
                Text(
                    text = "Mouvements de caisse",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }, actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                }
            })
    }) {

        if (uiState.isTransactionLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), start = 2.dp, end = 2.dp)
        ) {

            Column {
                PeriodeSection(
                    uiState = uiState, onselectePerioChange = onSelectePeriodeChange
                )
                Spacer(modifier = Modifier.height(8.dp))
                uiState.mouvementsFiltres?.let { transactions -> TransactionTable1(transactions = transactions) }
            }

        }

    }
}


@Composable
private fun TransactionTable1(
    transactions: List<SoldeMouvement>,
    onRowClick: (SoldeMouvement) -> Unit = {},
) {


    val headers = listOf("Type", "Devise", "Montant", "Avant", "Après", "Motif", "Date", "Agent")

    Table(
        columnCount = headers.size,
        headers = headers,
        items = transactions,
        onRowClick = onRowClick,
    ) { columnIndex, _, item ->

        when (columnIndex) {
            0 -> Text(item.motif)
            1 -> Text(item.devise)
            2 -> Text(item.montantChange.toString())
            3 -> Text(item.montantAvant.toString())
            4 -> Text(item.montantApres.toString())
            5 -> Text(item.soldeType)
            6 -> Text(Constants.formatTimestamp(item.dateMouvement))
            7 -> Text(item.auteurName)

            else -> {}
        }

    }

}


@Composable
private fun PeriodeSection(
    uiState: AdminTransactionUiState,
    onselectePerioChange: (PeriodeFiltre) -> Unit = {},

    ) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 12.dp)

        ) {
            PeriodeFiltre.entries.forEachIndexed { index, periode ->
                SegmentedButton(
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary


                    ),

                    label = {
                        Text(
                            periode.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (uiState.periodeFiltre == periode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onselectePerioChange(periode)
                    },
                    selected = uiState.periodeFiltre == periode,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index, count = PeriodeFiltre.entries.size
                    ),
                )

            }
        }
    }
}
