package org.megamind.mycashpoint.ui.screen.auth.url

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.megamind.mycashpoint.ui.component.CustomOutlinedTextField
import org.megamind.mycashpoint.ui.component.CustomerButton
import org.megamind.mycashpoint.ui.navigation.Destination
import org.megamind.mycashpoint.ui.screen.auth.etablissement.EtablissementViewModel
import org.megamind.mycashpoint.ui.screen.main.MyCashPointApp
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme


@Composable
fun UrlScreen(
    modifier: Modifier = Modifier,
    viewModel: UrlViewModel = koinViewModel(),
    navController: NavController
) {

    val navBackStackEntry = remember {
        navController.getBackStackEntry(Destination.ETABLISSEMENT.name)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val etablissementViewModel: EtablissementViewModel =
        koinViewModel(viewModelStoreOwner = navBackStackEntry)
    val etablissementUiState by etablissementViewModel.uiState.collectAsStateWithLifecycle()

    val telephone = etablissementUiState.contact



    UrlScreenContent(
        contact = telephone,
        uiState = uiState,
        onUrlChange = viewModel::onUrlChange
    )


}

@Composable
fun UrlScreenContent(contact: String, uiState: UrlUiState, onUrlChange: (String) -> Unit = {}) {

    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {


            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "Nous vous avons envoyer votre URL personnelle à votre numéro au ${contact},veillez le saisir ici",
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodySmall
                )
                CustomOutlinedTextField(
                    value = uiState.url,
                    onValueChange = onUrlChange,
                    label = "Url du serveur",
                    placeholder = "Ex: https:/10.052.0245.02/mycashpoint.com/123456789"

                )
                Spacer(modifier = Modifier.height(44.dp))
                CustomerButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text("Continuer")
                }
            }


        }
    }

}


@Composable
@Preview
fun UrlScreenPreview() {
    MyCashPointTheme {
        UrlScreenContent(contact = "+225 00 000 0000", uiState = UrlUiState())
    }

}


