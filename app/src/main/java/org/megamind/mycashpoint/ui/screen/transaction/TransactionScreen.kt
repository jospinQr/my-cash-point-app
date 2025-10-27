package org.megamind.mycashpoint.ui.screen.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold

import androidx.compose.material3.SegmentedButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.screen.operateur.OperateurUiState
import org.megamind.mycashpoint.ui.screen.operateur.OperateurViewModel
import org.megamind.mycashpoint.utils.Constants
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: TransationViewModel = koinViewModel()
) {

    val uiState by viewModel.uiSate.collectAsStateWithLifecycle()

    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Destination.OPERATEUR.name)
    }


    val operateurViewModel: OperateurViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
    val operateurUiState by operateurViewModel.uiState.collectAsStateWithLifecycle()

    TransactionScreenContent(
        uiState = uiState,
        sharedTransitionScope = sharedTransitionScope,
        operateurUiState = operateurUiState,
        animatedVisibilityScope = animatedVisibilityScope,
        onSelectedDevise = viewModel::onDeviseSelected
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun TransactionScreenContent(
    uiState: TransactionUiState,
    sharedTransitionScope: SharedTransitionScope,
    operateurUiState: OperateurUiState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSelectedDevise: (Constants.Devise) -> Unit

) {

    val selectedOperateur = operateurUiState.selectedOperateur


    with(sharedTransitionScope) {

        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        selectedOperateur?.name ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(
                                    key = operateurUiState.selectedOperateur?.id ?: ""
                                ),
                                animatedVisibilityScope
                            )
                    )
                },
                navigationIcon = {
                    Image(

                        modifier = Modifier
                            .size(42.dp)
                            .sharedElement(
                                rememberSharedContentState(
                                    operateurUiState.selectedOperateur?.name ?: ""
                                ), animatedVisibilityScope
                            )
                            .clip(CircleShape),
                        painter = painterResource(selectedOperateur!!.logo),
                        contentDescription = selectedOperateur.name

                    )
                }
            )
        }) { innerPadding ->


            Box(modifier = Modifier.padding(innerPadding)) {

                Column {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp)
                    ) {

                        Constants.Devise.entries.forEachIndexed { index, devise ->

                            SegmentedButton(
                                colors = SegmentedButtonDefaults.colors(),

                                label = {
                                    Text(devise.name)
                                },
                                onClick = {

                                    onSelectedDevise(devise)
                                },
                                selected = uiState.selectedDevise == devise,
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = Constants.Devise.entries.size
                                ),

                                )


                        }


                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {

                        Column(Modifier.padding(horizontal = 12.dp)) {

                            CustomOutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = "Montant",
                                placeholder = "10"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomOutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = "Montant",
                                placeholder = "10"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomOutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = "Montant",
                                placeholder = "10"
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                        }

                    }
                }

            }

        }

    }
}


