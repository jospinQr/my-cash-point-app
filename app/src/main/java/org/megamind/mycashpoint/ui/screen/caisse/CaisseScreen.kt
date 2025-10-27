package org.megamind.mycashpoint.ui.screen.caisse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CaisseScreen() {


    CaisseScreenContent()


}

@Composable
fun CaisseScreenContent() {


    Scaffold { innerPadding ->


        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


            Text("Caisse")

        }


    }

}


