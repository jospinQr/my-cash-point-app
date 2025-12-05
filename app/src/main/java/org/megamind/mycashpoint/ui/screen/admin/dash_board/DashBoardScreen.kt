package org.megamind.mycashpoint.ui.screen.admin.dash_board

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.HomeWork
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.TopOperateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.AnimatedPieChart
import org.megamind.mycashpoint.ui.component.PieChartData
import org.megamind.mycashpoint.ui.component.SkeletonLoadingEffect
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.ui.component.dynamicPieColor
import org.megamind.mycashpoint.ui.component.getPieChartColor
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.toMontant


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashBoardViewModel = koinViewModel(),
    navigateToCreateAgence: () -> Unit,
    navigateToCreateAgent: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    DashBoardScreenContent(
        uiState = uiState,
        onSelectedAgence = viewModel::onSelectedAgence,
        onAgenceDropdownExpanded = viewModel::onAgenceDropdownExpanded,
        onSelectedOperateurChange = viewModel::onSelectedOperateurChange,
        onSelectedDeviseChange = viewModel::onSelectedDeviseChange,
        onSelectedSoldeTypeChange = viewModel::onSelectedSoldeTypeChange,
        navigateToCreateAgence = navigateToCreateAgence,
        navigateToCreateAgent = navigateToCreateAgent

    )

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreenContent(
    uiState: DashBoardUiState,
    onSelectedAgence: (Agence) -> Unit = {},
    onAgenceDropdownExpanded: (Boolean) -> Unit = {},
    onSelectedOperateurChange: (Operateur) -> Unit = {},
    onSelectedDeviseChange: (Constants.Devise) -> Unit = {},
    onSelectedSoldeTypeChange: (SoldeType) -> Unit = {},
    navigateToCreateAgence: () -> Unit = {},
    navigateToCreateAgent: () -> Unit = {},
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = .06f
                )
            ),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Agence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (uiState.isAgenceLoading) {
                        SkeletonLoadingEffect(
                            modifier = Modifier
                                .width(102.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    } else if (uiState.agenceErrorMessage != null) {
                        Text(uiState.agenceErrorMessage)
                    } else {
                        TextDropdown(
                            items = uiState.agenceList,
                            selectedItem = uiState.selectedAgence,
                            onItemSelected = onSelectedAgence,
                            expanded = uiState.isAgenceDropDownExpanded,
                            onExpandedChange = onAgenceDropdownExpanded,
                            getText = { it.designation }
                        )
                    }


                }
            })
    }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 2.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {


            if (uiState.isAgenceLoading) {
                DashBoardLoadingSkeleton()
            } else if (uiState.agenceErrorMessage != null) {

                Text(uiState.agenceErrorMessage)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    OperateurSection(uiState, onSelectedOperateurChange)
                    Spacer(modifier = Modifier.height(8.dp))
                    DeviseSection(
                        uiState = uiState,
                        onSelectedDeviseChange = { onSelectedDeviseChange(it) })
                    Spacer(modifier = Modifier.height(8.dp))
                    PieCharSection(uiState)
                    Spacer(Modifier.height(8.dp))
                    SoldeSection(
                        uiState = uiState,
                        onSelectedSoldeTypeChange = { onSelectedSoldeTypeChange(it) }
                    )
                    Spacer(Modifier.height(8.dp))
                    ActionRappide(
                        uiState = uiState,
                        onCreateAgenceClick = navigateToCreateAgence,
                        onCreateAgentClick = navigateToCreateAgent
                    )
                }
            }
        }
    }
}

@Composable
fun ActionRappide(
    modifier: Modifier = Modifier,
    uiState: DashBoardUiState,
    onCreateAgenceClick: () -> Unit,
    onCreateAgentClick: () -> Unit
) {


    data class Action(val icon: ImageVector, val text: String)

    val actions = listOf(
        Action(Icons.Rounded.HomeWork, "Créer une agence"),
        Action(Icons.Default.Highlight, "Agence le plus perfomant"),
        Action(Icons.Default.PeopleOutline, "Créer un Agent"),
        Action(Icons.Default.Warning, "Solde en rupture")

    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(4),
            userScrollEnabled = false
        ) {

            itemsIndexed(items = actions) { index, item ->

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = {
                            when (index) {
                                0 -> onCreateAgenceClick()
                                2 -> onCreateAgentClick()
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .09f)
                        )
                    ) { Icon(imageVector = item.icon, contentDescription = null) }
                    Text(
                        item.text,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }


        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SoldeSection(
    uiState: DashBoardUiState,
    onSelectedSoldeTypeChange: (SoldeType) -> Unit,

    ) {

    if (uiState.currenteSolde == null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text("Aucun solde, verifier vos critères de recherche")
        }
        return
    }

    if (uiState.isSoldeLoading) {

        SoldeCardSkeleton()
        return
    }


    val solde = uiState.currenteSolde
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)


    )
    {


        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        Text(
                            text = "Solde",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Box {
                            SingleChoiceSegmentedButtonRow(
                                modifier = Modifier.height(32.dp)
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
                                                color = if (uiState.selectedSoldeType == soldeType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                                            )
                                        },
                                        onClick = {
                                            onSelectedSoldeTypeChange(soldeType)


                                        },
                                        selected = uiState.selectedSoldeType == soldeType,
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = index,
                                            count = Constants.Devise.entries.size
                                        ),

                                        )


                                }


                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.soldeErrorMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = uiState.soldeErrorMessage,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
                            )
                        }
                        return@Box
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Image(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            painter = painterResource(
                                uiState.selectedOperateur?.logo ?: R.drawable.airtel_logo
                            ),
                            contentDescription = null,
                        )

                        Column {
                            Text(
                                "${uiState.selectedOperateur?.name} ${solde.soldeType.name.lowercase()} ${solde.devise.name}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Row {
                                Text(
                                    "Dernier mis à jour ${Constants.formatTimestamp(solde.dernierMiseAJour)}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )

                            }
                        }

                    }

                    Spacer(Modifier.height(8.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Text("Solde")
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                Modifier.background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            ) {
                                Text(
                                    "${solde.montant.toMontant()} ${uiState.selectedDevise.symbole}",
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
                                "${solde.seuilAlerte ?: "0"} ${solde.devise.symbole}",
                                color = Color.Red
                            )
                        }
                    }


                }


            }
        }

    }
}

@Composable
private fun PieCharSection(uiState: DashBoardUiState) {

    if (uiState.isTopOperateurLoading) {
        PieChartSkeleton()
        return
    }

    if (uiState.topOperateurErrorMessage != null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(uiState.topOperateurErrorMessage)
        }
    }
    val colors = listOf(

        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.inversePrimary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.error,

        )


    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
        Card(
            modifier = Modifier,
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(
                    "Répartition des transaction par operateur",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                if (uiState.topOperateur.isNotEmpty()) {
                    AnimatedPieChart(
                        modifier = Modifier.fillMaxWidth(),
                        data = uiState.topOperateur
                            .mapIndexed { index, operateur ->
                                PieChartData(
                                    label = operateur.operateurNom,
                                    value = operateur.nombreTransactions.toFloat(),
                                    color = colors[index]
                                )
                            },
                        showLegend = true,
                        centerText = "Top operateur"
                    )
                }
            }


        }
    }


}

@Composable
private fun DeviseSection(
    uiState: DashBoardUiState,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
) {
    Box {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 12.dp)

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
}

@Composable
private fun OperateurSection(
    uiState: DashBoardUiState,
    onSelectedOperateurChange: (Operateur) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    )
    {

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
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashBoardScreenPreview() {

    MyCashPointTheme {

        DashBoardScreenContent(

            uiState = DashBoardUiState(
                currenteSolde = Solde(),
                selectedAgence = Agence("AGO1", "Bbo Centre"),
                selectedOperateur = operateurs.firstOrNull(),
                selectedDevise = Constants.Devise.CDF,
                isAgenceDropDownExpanded = false,
                agenceErrorMessage = null,
                isAgenceLoading = false,
                agenceList = listOf(
                    Agence("AGO1", "Bbo Centre"),
                    Agence("AGO2", "Bbo Est"),
                    Agence("AGO3", "Bbo Ouest"),
                    Agence("AGO4", "Bbo Sud"),
                    Agence("AGO5", "Bbo Nord"),

                    ),
                topOperateur = listOf(
                    TopOperateur(
                        operateurNom = "Airtel Money",
                        nombreTransactions = 10,
                    ),
                    TopOperateur(
                        operateurNom = "Orange Money",
                        nombreTransactions = 13,
                    ),

                    TopOperateur(
                        operateurNom = "Vodacom M-Pesa",
                        nombreTransactions = 4,
                    ),

                    TopOperateur(
                        operateurNom = "Equity",
                        nombreTransactions = 10,

                        ),


                    )

            ),


            )


    }
}