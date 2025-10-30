package org.megamind.mycashpoint.ui.screen.operateur

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OperateurScreen(
    modifier: Modifier = Modifier,
    viewModel: OperateurViewModel = koinViewModel(),
    navigateToTransactionScreen: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope

) {


    OperateurScreenContent(
        onOperateurSelected = viewModel::onOperateurSelected,
        onNavigateToTransactionScreen = navigateToTransactionScreen,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun OperateurScreenContent(
    modifier: Modifier = Modifier,
    onOperateurSelected: (Operateur) -> Unit,
    onNavigateToTransactionScreen: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope

) {


    Scaffold(topBar = { TopAppBar(title = { Text("Choisissez un opérateur") }) }) { innerPadding ->

        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
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
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {

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
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = operateur.id),
                            animatedVisibilityScope
                        ),
                    text = operateur.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

        }

    }
}
