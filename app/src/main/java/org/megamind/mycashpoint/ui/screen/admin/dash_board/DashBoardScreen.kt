package org.megamind.mycashpoint.ui.screen.admin.dash_board

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AddAlert
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
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
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.PieChartData
import org.megamind.mycashpoint.ui.component.SkeletonLoadingEffect
import org.megamind.mycashpoint.ui.component.SnackbarType
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.toMontant


@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashBoardViewModel = koinViewModel(),
    navigateToCreateAgence: () -> Unit,
    navigateToCreateAgent: () -> Unit,
    snackbarHostState: SnackbarHostState,
    navigateToLoginScreen: () -> Unit

) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandelUiEvent(viewModel, snackbarHostState, navigateToLoginScreen)

    DashBoardScreenContent(
        uiState = uiState,
        onSelectedAgence = viewModel::onSelectedAgence,
        onAgenceDropdownExpanded = viewModel::onAgenceDropdownExpanded,
        onSelectedOperateurChange = viewModel::onSelectedOperateurChange,
        onSelectedDeviseChange = viewModel::onSelectedDeviseChange,
        onSelectedSoldeTypeChange = viewModel::onSelectedSoldeTypeChange,
        navigateToCreateAgence = navigateToCreateAgence,
        navigateToCreateAgent = navigateToCreateAgent,
        onSoldeInRuptureClick = viewModel::onSoldeInRuptureDialogShown,
        onInitSoldeClick = viewModel::onInitSoldeClick,
        onInitSoldeBottomDismiss = viewModel::onInitSoldeBottomDismiss,
        onInitSoldeTypeChange = viewModel::onInitSoldeTypeChange,
        onInitSelectedOperateurChange = viewModel::onInitSelectedOperateurChange,
        onInitSelectedDeviseChange = viewModel::onInitSelectedDeviseChange,
        onInitOperateurMenuExpanded = viewModel::onInitOperateurMenuExpanded,
        onInitOperateurMenuDismiss = viewModel::onInitOperateurMenuDismiss,
        onSoldeChange = viewModel::onSoldeChange,
        onSeuilChange = viewModel::onSeuilChange,
        onSaveClick = viewModel::onConfirmDialogShown,
        onLogOut = viewModel::onLogOut

    )

    SoldeInRuptureDialog(
        uiState = uiState,
        isDialogShown = uiState.isSoldeInRuptureDialogShown,
        onDismiss = viewModel::onSoldeInRuptureDialogDismiss,
        soldeInRupture = uiState.soldeInRupture
    )

    if (uiState.isSoldeSaveLoading) {
        LoadinDialog(text = "Enregistrement du solde")

    }

    SaveConfirmationDialog(uiState, viewModel)


}

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
    onSoldeInRuptureClick: () -> Unit = {},
    onInitSoldeClick: () -> Unit = {},
    onInitSoldeBottomDismiss: () -> Unit = {},
    onInitSoldeTypeChange: (SoldeType) -> Unit = {},
    onInitSelectedOperateurChange: (Operateur) -> Unit = {},
    onInitSelectedDeviseChange: (Constants.Devise) -> Unit = {},
    onInitOperateurMenuExpanded: () -> Unit = {},
    onInitOperateurMenuDismiss: () -> Unit = {},
    onSoldeChange: (String) -> Unit = {},
    onSeuilChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onLogOut: () -> Unit = {}


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


                    if (uiState.isAgenceLoading) {

                        SkeletonLoadingEffect(
                            modifier = Modifier
                                .width(82.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SkeletonLoadingEffect(
                            modifier = Modifier
                                .width(102.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )

                    } else {
                        Text(
                            "Agence",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        uiState.agenceErrorMessage,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
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
                        onCreateAgenceClick = navigateToCreateAgence,
                        onCreateAgentClick = navigateToCreateAgent,
                        onSoldeInRuptureClick = onSoldeInRuptureClick,
                        onInitSoldeClick = onInitSoldeClick,
                        onLogoutClick = onLogOut
                    )
                }
            }
        }



        InitSoldeBottomSheet(
            uiState = uiState,
            onInitSoldeBottomDismiss = onInitSoldeBottomDismiss,
            onSelectedDeviseChange = onInitSelectedDeviseChange,
            onSelectedOperateurChange = onInitSelectedOperateurChange,
            onOperateurMenuExpanded = onInitOperateurMenuExpanded,
            onOperateurMenuDismiss = onInitOperateurMenuDismiss,
            onConfirmDialogShown = onInitSoldeClick,
            onSoldeInitialChange = onSoldeChange,
            onSeuilChange = onSeuilChange,
            onSelectedTypeSoldeChange = onInitSoldeTypeChange,
            onSaveClick = onSaveClick


        )
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
                    Text(operateur.name, style = MaterialTheme.typography.bodySmall)
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
                            style = MaterialTheme.typography.bodySmall,
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
private fun PieCharSection(uiState: DashBoardUiState) {

    if (uiState.isTopOperateurLoading) {
        PieChartSkeleton()
        return
    }

    if (uiState.topOperateurErrorMessage != null) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(uiState.topOperateurErrorMessage)
        }
        return
    }

    if (uiState.topOperateur.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

            Text(
                "Aucune transaction pour l'instant",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
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
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
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
private fun SoldeSection(
    uiState: DashBoardUiState,
    onSelectedSoldeTypeChange: (SoldeType) -> Unit,

    ) {

    if (uiState.currenteSolde == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Aucun solde, verifier vos critères de recherche",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
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
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
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
                                                style = MaterialTheme.typography.bodySmall,
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
                            Text("Solde", style = MaterialTheme.typography.bodySmall)
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Seuil d'alerte", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "${solde.seuilAlerte ?: "0"} ${solde.devise.symbole}",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }


                }


            }
        }

    }
}


@Composable
fun ActionRappide(
    modifier: Modifier = Modifier,
    onCreateAgenceClick: () -> Unit,
    onCreateAgentClick: () -> Unit,
    onSoldeInRuptureClick: () -> Unit,
    onInitSoldeClick: () -> Unit,
    onLogoutClick: () -> Unit,

    ) {


    data class Action(val icon: ImageVector, val text: String)

    val actions = listOf(
        Action(Icons.Outlined.HomeWork, "Créer une agence"),
        Action(Icons.Outlined.PeopleOutline, "Créer un Agent"),
        Action(Icons.Outlined.MonetizationOn, "Initialiser le solde"),
        Action(Icons.Outlined.BarChart, "Agence le plus perfomant"),
        Action(Icons.Outlined.Warning, "Solde en rupture"),
        Action(Icons.Outlined.Logout, "Se deconnecter"),

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
                                1 -> onCreateAgentClick()
                                2 -> onInitSoldeClick()
                                3 -> onSoldeInRuptureClick()
                                5 -> onLogoutClick()

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoldeInRuptureDialog(

    uiState: DashBoardUiState,
    modifier: Modifier = Modifier,
    isDialogShown: Boolean,
    onDismiss: () -> Unit,
    soldeInRupture: List<Solde>
) {


    if (isDialogShown)
        AlertDialog(
            dismissButton = {},
            confirmButton = {},
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    "Solde en rupture",

                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )

            }, text = {
                Box() {

                    Box(
                        modifier = Modifier

                            .padding(12.dp),
                    ) {


                        if (uiState.isSoldeInRuptureLoading) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            return@Box

                        }
                        if (uiState.soldeInRuptureErrorMessage != null) {
                            Text(
                                modifier = Modifier.align(Alignment.TopCenter),
                                text = uiState.soldeInRuptureErrorMessage
                            )
                            return@Box
                        }

                        LazyColumn(
                            modifier = Modifier.padding(top = 12.dp)
                        ) {

                            items(soldeInRupture) { solde ->

                                ListItem(
                                    headlineContent = {
                                        operateurs.find { it.id == solde.idOperateur }?.name?.let {
                                            Text(it)
                                        }
                                    },
                                    overlineContent = {
                                        Text("${solde.montant} ${solde.devise.symbole}")
                                    },
                                    trailingContent = {

                                        Text(solde.agenceCode)
                                    }

                                )


                            }

                        }


                    }
                }
            }

        )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun InitSoldeBottomSheet(
    uiState: DashBoardUiState,
    onInitSoldeBottomDismiss: () -> Unit,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
    onSelectedOperateurChange: (Operateur) -> Unit,
    onOperateurMenuExpanded: () -> Unit,
    onOperateurMenuDismiss: () -> Unit,
    onConfirmDialogShown: () -> Unit,
    onSoldeInitialChange: (String) -> Unit,
    onSeuilChange: (String) -> Unit,
    onSelectedTypeSoldeChange: (SoldeType) -> Unit,
    onSaveClick: () -> Unit


) {
    if (uiState.isInitSoldeBottomSheetShown) {

        ModalBottomSheet(
            onDismissRequest = { onInitSoldeBottomDismiss() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),

            ) {

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Initialiser le sole  ")
                    AnimatedContent(uiState.initSelectedOperateur) { operateur ->
                        Text(operateur.name)
                    }
                    AnimatedContent(uiState.initSelectedDevise) { devise ->
                        Text(devise.name)
                    }
                }
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Column {

                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier
                                .fillMaxWidth()

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
                                        Text(soldeType.name)
                                    },
                                    onClick = {

                                        onSelectedTypeSoldeChange(soldeType)
                                    },
                                    selected = uiState.initSoldeType == soldeType,
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = SoldeType.entries.size
                                    ),

                                    )


                            }


                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text("Devise ")
                            Constants.Devise.entries.forEachIndexed { index, devise ->

                                RadioButton(
                                    selected = uiState.initSelectedDevise == devise,
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
                            value = uiState.initSelectedOperateur.name,
                            onValueChange = {

                            },
                            leadingIcon = {
                                Image(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape),
                                    painter = painterResource(
                                        uiState.initSelectedOperateur.logo
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
                            expanded = uiState.isInitOperateurExpanded,
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
                        label = "Solde initial",
                        keyboardType = KeyboardType.Decimal,
                        isError = uiState.isSoldeError,
                        errorMessage = "Le solde initial doit être supérieur à 0"
                    )
                    CustomOutlinedTextField(
                        value = uiState.seuilAlert ?: "0",
                        onValueChange = {
                            onSeuilChange(it)
                        },
                        keyboardType = KeyboardType.Decimal,
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
}

@Composable
private fun SaveConfirmationDialog(
    uiState: DashBoardUiState,
    viewModel: DashBoardViewModel
) {
    if (uiState.isConfirmDialogShown) {
        AlertDialog(
            onDismissRequest = { viewModel.onConfirmDialogDismiss() },
            title = {
                Text(
                    "Confirmer l'initialisation",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text("Voulez-vous vraiment initialiser ce solde ?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Opérateur: ${uiState.initSelectedOperateur.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Type: ${uiState.initSoldeType.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Devise: ${uiState.initSelectedDevise.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Montant: ${uiState.solde}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            confirmButton = {
                CustomerButton(
                    onClick = {
                        viewModel.onSaveClick()
                        viewModel.onConfirmDialogDismiss()
                    }
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                CustomerButton(
                    onClick = { viewModel.onConfirmDialogDismiss() }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun HandelUiEvent(
    viewModel: DashBoardViewModel,
    snackbarHostState: SnackbarHostState,
    navigateToLoginScreen: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {

            when (it) {

                DashBoardUiEvent.NavigateToLogin -> {
                    navigateToLoginScreen()
                }

                is DashBoardUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        CustomSnackbarVisuals(it.errorMessage, type = SnackbarType.ERROR)
                    )
                }

                is DashBoardUiEvent.ShowSuccesMessage -> {
                    snackbarHostState.showSnackbar(
                        CustomSnackbarVisuals(it.successMessage, type = SnackbarType.SUCCESS)
                    )
                }
            }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashBoardScreenPreview() {

    MyCashPointTheme {

        DashBoardScreenContent(

            uiState = DashBoardUiState(
                currenteSolde = Solde(),
                selectedAgence = Agence("AGO1", "Bbo Centre"),
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




