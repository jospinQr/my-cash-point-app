package org.megamind.mycashpoint.ui.screen.admin.rapport

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
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
import org.megamind.mycashpoint.ui.component.StyledTopAppBar
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.ui.screen.admin.dash_board.DashBoardUiState
import org.megamind.mycashpoint.ui.screen.transaction.TransactionTypeButton
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.UtilsFonctions.Companion.openExcelFile
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

    // Handle PDF opening
    uiState.pdfToOpen?.let { bytes ->
        LaunchedEffect(bytes) {
            openPdfFile(context, bytes.first())
            viewModel.clearPdfEvent()
        }
    }

    // Handle Excel opening
    uiState.excelToOpen?.let { bytes ->
        LaunchedEffect(bytes) {
            openExcelFile(context, bytes.first())
            viewModel.clearExcelEvent()
        }
    }

    AdminRepportScreenContent(
        uiState = uiState,

        onSelectedAgence = viewModel::onSelectedAgence,
        onAgenceDropdownExpanded = viewModel::onAgenceDropdownExpanded,
        onReportClick = viewModel::onReportClick,
        onDismissReportDialog = viewModel::onDismissReportDialog,
        onDateFilterEnabledChange = viewModel::onDateFilterEnabledChange,
        onDialogStartDateChange = viewModel::onDialogStartDateChange,
        onDialogEndDateChange = viewModel::onDialogEndDateChange,
        onShowDialogStartDatePicker = viewModel::onShowDialogStartDatePicker,
        onDismissDialogStartDatePicker = viewModel::onDismissDialogStartDatePicker,
        onShowDialogEndDatePicker = viewModel::onShowDialogEndDatePicker,
        onDismissDialogEndDatePicker = viewModel::onDismissDialogEndDatePicker,
        onConfirmReportGeneration = viewModel::onConfirmReportGeneration
    )

    // Show loading dialog for PDF or Excel
    if (uiState.isPdfLoading || uiState.isExcelLoading) {
        LoadinDialog()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRepportScreenContent(
    uiState: AdminRepportUiState,
    onSelectedAgence: (Agence) -> Unit = {},
    onAgenceDropdownExpanded: (Boolean) -> Unit = {},
    onReportClick: (Rapports) -> Unit = {},
    onDismissReportDialog: () -> Unit = {},
    onDateFilterEnabledChange: (Boolean) -> Unit = {},
    onDialogStartDateChange: (LocalDate) -> Unit = {},
    onDialogEndDateChange: (LocalDate) -> Unit = {},
    onShowDialogStartDatePicker: () -> Unit = {},
    onDismissDialogStartDatePicker: () -> Unit = {},
    onShowDialogEndDatePicker: () -> Unit = {},
    onDismissDialogEndDatePicker: () -> Unit = {},
    onConfirmReportGeneration: () -> Unit = {}
) {

    // Show Report Date Dialog
    if (uiState.isReportDialogShown) {
        ReportDateDialog(
            uiState = uiState,
            onDismiss = onDismissReportDialog,
            onDateFilterEnabledChange = onDateFilterEnabledChange,
            onStartDateChange = onDialogStartDateChange,
            onEndDateChange = onDialogEndDateChange,
            onShowStartDatePicker = onShowDialogStartDatePicker,
            onDismissStartDatePicker = onDismissDialogStartDatePicker,
            onShowEndDatePicker = onShowDialogEndDatePicker,
            onDismissEndDatePicker = onDismissDialogEndDatePicker,
            onConfirm = onConfirmReportGeneration
        )
    }

    Scaffold(topBar = {
        StyledTopAppBar(
            title = "Dashboard",
            customTitleContent = {
                Text(
                    "Rapport ancenge",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                TextDropdown(
                    items = uiState.agenceList,
                    selectedItem = uiState.selectedAgence,
                    onItemSelected = onSelectedAgence,
                    expanded = uiState.isAgenceDropDownExpanded,
                    onExpandedChange = onAgenceDropdownExpanded,
                    getText = { it.designation }
                )
            }
        )
    }) { it ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),

            ) {


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                LazyVerticalGrid(

                    modifier = Modifier
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(2)
                ) {


                    itemsIndexed(Rapports.entries) { index, typTransct ->


                        TransactionTypeButton(
                            onClick = { onReportClick(typTransct) },
                            label = typTransct.label,
                            icon = typTransct.icon
                        )

                    }
                }


            }
        }
    }
}


@Serializable
enum class Rapports(val icon: Int, val label: String) {
    JOURNAL_TRANSACT(R.drawable.depot, "Journal des transactions"),
    JOURNAL_OPERATION(R.drawable.retrait, "Journal des operation interne"),
    GRAND_LIVRE(R.drawable.retrait, "Grand livre"),


}


@Preview(showBackground = true)
@Composable
fun RapportAdminContentPreview() {

    val mockUistate = AdminRepportUiState()
    MyCashPointTheme {

        AdminRepportScreenContent(uiState = mockUistate)
    }

}

/**
 * Dialog for selecting date range before printing Excel reports
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDateDialog(
    uiState: AdminRepportUiState,
    onDismiss: () -> Unit,
    onDateFilterEnabledChange: (Boolean) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onShowStartDatePicker: () -> Unit,
    onDismissStartDatePicker: () -> Unit,
    onShowEndDatePicker: () -> Unit,
    onDismissEndDatePicker: () -> Unit,
    onConfirm: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val alpha by animateFloatAsState(
        targetValue = if (uiState.isDateFilterEnabled) 1f else 0.5f,
        animationSpec = tween(300),
        label = "alpha"
    )

    // Start Date Picker Dialog
    if (uiState.isDialogStartDatePickerShown) {
        val startDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.dialogStartDate.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = onDismissStartDatePicker,
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate =
                            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        onStartDateChange(selectedDate)
                    }
                    onDismissStartDatePicker()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissStartDatePicker) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    // End Date Picker Dialog
    if (uiState.isDialogEndDatePickerShown) {
        val endDatePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = uiState.dialogEndDate.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = onDismissEndDatePicker,
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate =
                            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        onEndDateChange(selectedDate)
                    }
                    onDismissEndDatePicker()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissEndDatePicker) {
                    Text("Annuler")
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Header with icon and title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Générer le rapport",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        uiState.selectedReportType?.let { report ->
                            Text(
                                text = report.label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(20.dp))

                // Date filter toggle section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDateFilterEnabledChange(!uiState.isDateFilterEnabled)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Filtrer par date",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (uiState.isDateFilterEnabled) "Intervalle personnalisé" else "Toutes les données",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = uiState.isDateFilterEnabled,
                            onCheckedChange = onDateFilterEnabledChange,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }

                // Date picker fields with animation
                AnimatedVisibility(
                    visible = uiState.isDateFilterEnabled,
                    enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                    exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        // Start Date Field
                        DateFieldCard(
                            label = "Date de début",
                            dateValue = uiState.dialogStartDate.format(dateFormatter),
                            onClick = onShowStartDatePicker
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // End Date Field
                        DateFieldCard(
                            label = "Date de fin",
                            dateValue = uiState.dialogEndDate.format(dateFormatter),
                            onClick = onShowEndDatePicker
                        )

                        // Date range info
                        Spacer(modifier = Modifier.height(12.dp))
                        val daysDiff = java.time.temporal.ChronoUnit.DAYS.between(
                            java.time.LocalDate.of(
                                uiState.dialogStartDate.year,
                                uiState.dialogStartDate.monthValue,
                                uiState.dialogStartDate.dayOfMonth
                            ),
                            java.time.LocalDate.of(
                                uiState.dialogEndDate.year,
                                uiState.dialogEndDate.monthValue,
                                uiState.dialogEndDate.dayOfMonth
                            )
                        )
                        Text(
                            text = "Période de $daysDiff jours",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Annuler")
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Imprimer")
                    }
                }
            }
        }
    }
}

/**
 * Styled date field card component
 */
@Composable
private fun DateFieldCard(
    label: String,
    dateValue: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateValue,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Sélectionner la date",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}