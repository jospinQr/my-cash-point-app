package org.megamind.mycashpoint.ui.screen.admin.rapport

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SkeletonLoadingEffect
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.ui.screen.admin.dash_board.DashBoardUiState
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.UtilsFonctions.Companion.openPdfFile
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


@Composable
fun AdminRepportScreen(
    modifier: Modifier = Modifier, viewModel: AdminRapportViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    uiState.pdfToOpen?.let { bytes ->
        LaunchedEffect(bytes) {
            openPdfFile(context, bytes.first())
            viewModel.clearPdfEvent() // reset pour ne pas rouvrir
        }
    }
    AdminRepportScreenContent(
        uiState = uiState,
        onSelectedDeviseChange = viewModel::onSelectedDeviseChange,
        onSelectedAgence = viewModel::onSelectedAgence,
        onAgenceDropdownExpanded = viewModel::onAgenceDropdownExpanded,
        onSelectedOperateurChange = viewModel::onSelectedOperateurChange,
        onShowStartDatePicker = viewModel::onShowStartDatePicker,
        onDismissStartDatePicker = viewModel::onDismissStartDatePicker,
        onShowEndDatePicker = viewModel::onShowEndDatePicker,
        onDismissEndDatePicker = viewModel::onDismissEndDatePicker,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onSelectedTransactTypeChange = viewModel::onSelectedTransactTypeChange,
        onTransactTypeDropdownExpanded = viewModel::onTransactTypeDropdownExpanded,
        onGenerateReport = viewModel::generateReport
    )

    if (uiState.isPdfLoading) {
        LoadinDialog()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRepportScreenContent(
    uiState: AdminRepportUiState,
    onSelectedAgence: (Agence) -> Unit = {},
    onAgenceDropdownExpanded: (Boolean) -> Unit = {},
    onSelectedDeviseChange: (Constants.Devise) -> Unit = {},
    onSelectedOperateurChange: (Operateur) -> Unit = {},
    onShowStartDatePicker: () -> Unit = {},
    onDismissStartDatePicker: () -> Unit = {},
    onShowEndDatePicker: () -> Unit = {},
    onDismissEndDatePicker: () -> Unit = {},
    onStartDateChange: (LocalDate) -> Unit = {},
    onEndDateChange: (LocalDate) -> Unit = {},
    onSelectedTransactTypeChange: (TransactionType) -> Unit = {},
    onTransactTypeDropdownExpanded: (Boolean) -> Unit = {},
    onGenerateReport: () -> Unit = {}
) {
    Scaffold(topBar = {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = .06f
                )
            ), title = {

                Text("Générer les transaction en pdf")
            })
    }) { it ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            ) {

            AgenceSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                uiState,
                onSelectedAgence,
                onAgenceDropdownExpanded
            )

            CustomerButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                onClick = {
                    onGenerateReport()
                }
            ) {
                Row {
                    Text("Générer Pdf")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Rounded.Print, contentDescription = null)
                }

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {


                    Spacer(modifier = Modifier.height(8.dp))
                    TypeTransactionSection(
                        uiState = uiState,
                        onSelectedTransactTypeChange = onSelectedTransactTypeChange,
                        onTransactTypeDropdownExpanded = onTransactTypeDropdownExpanded
                    )
                    OperateurSection(uiState = uiState, onSelectedOperateurChange)
                    Spacer(modifier = Modifier.height(8.dp))
                    DeviseSection(
                        uiState = uiState, onSelectedDeviseChange = { onSelectedDeviseChange(it) })
                    Spacer(modifier = Modifier.height(8.dp))
                    DateRangeSection(
                        uiState = uiState,
                        onShowStartDatePicker = onShowStartDatePicker,
                        onDismissStartDatePicker = onDismissStartDatePicker,
                        onShowEndDatePicker = onShowEndDatePicker,
                        onDismissEndDatePicker = onDismissEndDatePicker,
                        onStartDateChange = onStartDateChange,
                        onEndDateChange = onEndDateChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                }


            }
        }
    }
}

@Composable
fun TypeTransactionSection(
    uiState: AdminRepportUiState,
    onSelectedTransactTypeChange: (TransactionType) -> Unit = {},
    onTransactTypeDropdownExpanded: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Text("Type")
        Spacer(modifier = Modifier.width(4.dp))
        TextDropdown(
            items = TransactionType.entries,
            selectedItem = uiState.selectedType,
            onItemSelected = onSelectedTransactTypeChange,
            expanded = uiState.isTransactTypeDropDownExpanded,
            onExpandedChange = onTransactTypeDropdownExpanded,
            getText = { it.name })


    }

}

@Composable
private fun AgenceSection(
    modifier: Modifier = Modifier,
    uiState: AdminRepportUiState,
    onSelectedAgence: (Agence) -> Unit,
    onAgenceDropdownExpanded: (Boolean) -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Selctionner une agence",
            maxLines = 1,
            overflow = TextOverflow.Clip,
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
                getText = { it.designation })
        }


    }
}


@Composable
private fun DeviseSection(
    uiState: AdminRepportUiState,
    onSelectedDeviseChange: (Constants.Devise) -> Unit,
) {
    Box {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(horizontal = 12.dp)

        ) {

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
                        index = index, count = Constants.Devise.entries.size
                    ),

                    )


            }


        }
    }
}

@Composable
private fun OperateurSection(
    uiState: AdminRepportUiState, onSelectedOperateurChange: (Operateur) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        operateurs.forEachIndexed { index, operateur ->

            ElevatedFilterChip(
                elevation = FilterChipDefaults.elevatedFilterChipElevation(
                    elevation = 8.dp
                ), colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary

                ), selected = uiState.selectedOperateur == operateur, onClick = {
                    onSelectedOperateurChange(operateur)
                }, label = {
                    Text(operateur.name)
                }, leadingIcon = {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSection(
    uiState: AdminRepportUiState,
    onShowStartDatePicker: () -> Unit = {},
    onDismissStartDatePicker: () -> Unit = {},
    onShowEndDatePicker: () -> Unit = {},
    onDismissEndDatePicker: () -> Unit = {},
    onStartDateChange: (LocalDate) -> Unit = {},
    onEndDateChange: (LocalDate) -> Unit = {},
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Start Date Picker Dialog
    if (uiState.isStartDatePickerShown) {
        val startDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.startDate.atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(onDismissRequest = onDismissStartDatePicker, confirmButton = {
            TextButton(onClick = {
                startDatePickerState.selectedDateMillis?.let { millis ->
                    val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onStartDateChange(selectedDate)
                }
                onDismissStartDatePicker()
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = onDismissStartDatePicker) {
                Text("Annuler")
            }
        }) {
            DatePicker(state = startDatePickerState)
        }
    }

    // End Date Picker Dialog
    if (uiState.isEndDatePickerShown) {
        val endDatePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = uiState.endDate.atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        )
        DatePickerDialog(onDismissRequest = onDismissEndDatePicker, confirmButton = {
            TextButton(onClick = {
                endDatePickerState.selectedDateMillis?.let { millis ->
                    val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onEndDateChange(selectedDate)
                }
                onDismissEndDatePicker()
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = onDismissEndDatePicker) {
                Text("Annuler")
            }
        }) {
            DatePicker(state = endDatePickerState)
        }
    }

    Row(Modifier.fillMaxWidth()) {
        CustomOutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .clickable { onShowStartDatePicker() },
            label = "Date début",
            value = uiState.startDate.format(dateFormatter),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { onShowStartDatePicker() },
                    imageVector = Icons.Default.DateRange,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            })
        Spacer(modifier = Modifier.width(8.dp))
        CustomOutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .clickable { onShowEndDatePicker() },
            label = "Date fin",
            value = uiState.endDate.format(dateFormatter),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { onShowEndDatePicker() },
                    imageVector = Icons.Default.DateRange,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            })
    }
}


@Preview(showBackground = true)
@Composable
fun RapportAdminContentPreview() {

    val mockUistate = AdminRepportUiState()
    MyCashPointTheme {

        AdminRepportScreenContent(uiState = mockUistate)
    }

}