package org.megamind.mycashpoint.ui.screen.admin.transaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AdminTransactionScreen(modifier: Modifier = Modifier) {

    AdminTransactionScreenContentScreen()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTransactionScreenContentScreen() {

    Scaffold(topBar = { TopAppBar(title = { Text("Transactions") }) }) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Transactions")

        }


    }

}