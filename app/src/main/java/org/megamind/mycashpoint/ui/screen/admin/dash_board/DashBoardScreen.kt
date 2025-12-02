package org.megamind.mycashpoint.ui.screen.admin.dash_board

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.ui.component.TextDropdown
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme

@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashBoardViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getAllAgence()
    }

    DashBoardScreenContent(
        uiState = uiState,
        onSelectedAgence = viewModel::onSelectedAgence,
        onAgenceDropdownExpanded = viewModel::onAgenceDropdownExpanded
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreenContent(
    uiState: DashBoardUiState,
    onSelectedAgence: (Agence) -> Unit,
    onAgenceDropdownExpanded: (Boolean) -> Unit
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .06f)),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Agence",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    when {
                        uiState.isAgenceLoading -> CircularProgressIndicator()
                        uiState.agenceErrorMessage != null -> Text(uiState.agenceErrorMessage)
                        else -> TextDropdown(
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
                .padding(it), contentAlignment = Alignment.Center
        ) {

            uiState.selectedAgence?.designation?.let { text -> Text(text = text) }


        }

    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashBoardScreenPreview() {

    MyCashPointTheme {

        DashBoardScreenContent(
            onAgenceDropdownExpanded = {},
            onSelectedAgence = {},
            uiState = DashBoardUiState(
                agenceErrorMessage = null,
                isAgenceLoading = false,
                agenceList = listOf(
                    Agence("AGO1", "Bbo Centre"),
                    Agence("AGO2", "Bbo Est"),
                    Agence("AGO3", "Bbo Ouest"),
                    Agence("AGO4", "Bbo Sud"),
                    Agence("AGO5", "Bbo Nord"),

                    )
            )
        )


    }
}