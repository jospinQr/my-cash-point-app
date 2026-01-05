package org.megamind.mycashpoint.ui.screen.operateur

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import androidx.compose.runtime.getValue
import org.megamind.mycashpoint.ui.component.ConfirmDialog
import org.megamind.mycashpoint.ui.component.CustomSnackbar
import org.megamind.mycashpoint.ui.component.CustomSnackbarVisuals
import org.megamind.mycashpoint.ui.component.LoadinDialog
import org.megamind.mycashpoint.ui.component.SnackbarType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OperateurScreen(
    modifier: Modifier = Modifier,
    viewModel: OperateurViewModel = koinViewModel(),
    navigateToTransactionScreen: () -> Unit,
    navigateToSignIn: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    snackBarHostState: SnackbarHostState

) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {

        viewModel.uiEvent.collect {
            when (it) {
                OperateurUiEvent.NavigateToLogin -> {
                    navigateToSignIn()
                }

                is OperateurUiEvent.ShowError -> {
                    snackBarHostState.showSnackbar(
                        visuals = CustomSnackbarVisuals(
                            it.message,
                            SnackbarType.ERROR
                        )
                    )
                }
            }
        }

    }

    OperateurScreenContent(
        uiState = uiState,
        onOperateurSelected = viewModel::onOperateurSelected,
        onNavigateToTransactionScreen = navigateToTransactionScreen,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onLogoutClick = viewModel::onLogOut,
        onIsConfirmLogOutDialogDismiss = viewModel::onConfirmLogOutDialogDismiss,
        onIsConfirmLogOutDialogShown = viewModel::onConfirmLogOutDialogShown,
        onMainMenuHidden = viewModel::onMainMenuHidden,
        onMainMenuExpanded = viewModel::onMainMenuExpanded,
        onIsConfirmDownLoadDialogShown = viewModel::onConfirmDownLoadDialogShown,
        onIsConfirmDownLoadDialogHidden = viewModel::onConfirmDownLoadDialogHidden,
        downloadAllData = viewModel::getAllSoldeFromServerAndInsertInLocaldb,
        getEtsInfo = viewModel::getEtablissement

    )

    if (uiState.isGetAllSoldeLoading) {
        LoadinDialog(text = "Téléchargement solde en cours")
    }
    if (uiState.isGetAllTransactLoading) {
        LoadinDialog(text = "Téléchargement des transactions en cours")
    }
    if (uiState.isInsertAllSoldeLoading) {
        LoadinDialog(text = "Insertion des soldes en cours")
    }

    if (uiState.isInsertAllTransactLoading) {
        LoadinDialog(text = "Insertion des transactions en cours")
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun OperateurScreenContent(
    uiState: OperateurUiState,
    modifier: Modifier = Modifier,
    onOperateurSelected: (Operateur) -> Unit,
    onNavigateToTransactionScreen: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onIsConfirmLogOutDialogShown: () -> Unit,
    onIsConfirmLogOutDialogDismiss: () -> Unit,
    onLogoutClick: () -> Unit,
    onMainMenuExpanded: () -> Unit,
    onMainMenuHidden: () -> Unit,
    onIsConfirmDownLoadDialogShown: () -> Unit,
    onIsConfirmDownLoadDialogHidden: () -> Unit,
    downloadAllData: () -> Unit,
    getEtsInfo: () -> Unit


) {


    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Choisissez un opérateur",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }, actions = {
            IconButton(onClick = { onMainMenuExpanded() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = null
                )
            }

            DropdownMenu(
                expanded = uiState.isMainMenuExpanded, onDismissRequest = { onMainMenuHidden() }) {

                mainMenu.forEachIndexed { index, label ->
                    DropdownMenuItem(
                        text = { Text(text = label) },
                        onClick = {
                            when (index) {
                                0 -> {
                                    onIsConfirmDownLoadDialogShown()
                                }

                                1 -> {
                                    getEtsInfo()

                                }

                                2 -> {
                                    onIsConfirmLogOutDialogShown()
                                }


                            }
                        }
                    )


                }


            }

        }

        )
    }) { innerPadding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            LazyHorizontalGrid(
                modifier = Modifier.fillMaxWidth(),
                rows = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {

                items(operateurs) { operateur ->


                    OperateurItem(
                        modifier = Modifier,
                        operateur = operateur,
                        onOperateurSelected = {
                            onOperateurSelected(it)
                            onNavigateToTransactionScreen()

                        },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope

                    )

                }
            }

        }
    }


    ConfirmDialog(
        title = "Voulez-vous vous déconnecter ?",
        visible = uiState.isConfirmLogOutDialogShown,
        onDismiss = {
            onIsConfirmLogOutDialogDismiss()
        },
        onConfirm = {
            onLogoutClick()
            onIsConfirmLogOutDialogDismiss()
        },
    )

    ConfirmDialog(
        title = "Voulez-vous télécharger les transactions ?",
        visible = uiState.isConfirmDownloadDialogShown,
        onDismiss = {
            onIsConfirmDownLoadDialogHidden()
        },
        onConfirm = {
            downloadAllData()
            onIsConfirmDownLoadDialogHidden()

        }
    )


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OperateurItem(
    modifier: Modifier = Modifier,
    operateur: Operateur,
    onOperateurSelected: (Operateur) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope

) {

    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .width(180.dp)
                .height(24.dp)
                .background(operateur.color.copy(.08f))
                .clickable {
                    onOperateurSelected(operateur)
                }
                .padding(12.dp), contentAlignment = Alignment.Center) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = operateur.name),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    painter = painterResource(operateur.logo),
                    contentDescription = operateur.name
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = operateur.id), animatedVisibilityScope
                    ),
                    text = operateur.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

        }

    }
}


private val mainMenu = listOf(
    "Premier sync", "Actualiser Ets Info", "Se deconnecter"
)