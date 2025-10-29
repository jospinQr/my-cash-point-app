package org.megamind.mycashpoint.ui.Agence

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.data.data_source.local.entity.Agence
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.LoadinDialog

@Composable
fun AgenceScreen(
    modifier: Modifier = Modifier,
    viewModel: AgenceViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val agences by viewModel.agences.collectAsStateWithLifecycle()


    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect { event ->
            when (event) {
                AgenceUiEvent.OnSaveOrUpdate -> {
                    Toast.makeText(context, "Enregistrement reussi", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AgenceScreenContent(
        uiState = uiState,
        onIdChange = viewModel::onIdChange,
        onDesignationChange = viewModel::onDesignationChange,
        onSaveOrUpdate = viewModel::onSaveOrUpdate,
        agences = agences,
        modifier = modifier,
        onFormHidden = viewModel::onFormHidden,
        onFormShown = viewModel::onFormShown

    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgenceScreenContent(
    uiState: AgenceUiState,
    onIdChange: (String) -> Unit,
    onDesignationChange: (String) -> Unit,
    onSaveOrUpdate: () -> Unit,
    agences: List<Agence>,
    onFormHidden: () -> Unit,
    onFormShown: () -> Unit,
    modifier: Modifier = Modifier
) {


    Scaffold(topBar = { TopAppBar(title = { Text("Agence") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { onFormShown() }) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            if (agences.isEmpty()) {
                Text(
                    text = "Aucune Agence",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
            LazyColumn(contentPadding = PaddingValues(12.dp)) {

                items(agences) { agence ->

                    ListItem(

                        headlineContent = { Text(agence.id) },
                        supportingContent = { Text(agence.designation) },
                        modifier = Modifier
                            .border(
                                width = .02.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }

        }
    }

    if (uiState.isLoadind) {

        LoadinDialog()
    }

    if (uiState.error != null) {
        Toast.makeText(LocalContext.current, uiState.error, Toast.LENGTH_SHORT).show()
    }


    if (uiState.isFomShown) {

        ModalBottomSheet(
            onDismissRequest = { onFormHidden() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {


            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "Ajouter une Agence")


                }


                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CustomOutlinedTextField(
                        value = uiState.id,
                        onValueChange = onIdChange,
                        label = "Id",
                        isError = uiState.isIdError,
                        errorMessage = "L'id est obligatoire"
                    )
                    CustomOutlinedTextField(
                        value = uiState.designation,
                        onValueChange = onDesignationChange,
                        label = "Designation",
                        isError = uiState.isDesignationError,
                        errorMessage = "La designation est obligatoire"
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(modifier = Modifier.fillMaxWidth(), onClick = onSaveOrUpdate) {
                        Text(text = "Enregistrer")
                    }
                }

            }

        }
    }


}

