package org.megamind.mycashpoint

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.megamind.mycashpoint.ui.screen.main.MyCashPointApp
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme

class MainActivity : ComponentActivity() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            MyCashPointTheme {

                MyCashPointApp()

            }
        }
    }
}

