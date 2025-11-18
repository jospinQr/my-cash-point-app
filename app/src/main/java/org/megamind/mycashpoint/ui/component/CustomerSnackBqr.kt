package org.megamind.mycashpoint.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable

fun CustomSnackbar(data: SnackbarData) {

    val visuals = data.visuals as CustomSnackbarVisuals

    val (bgColor, icon) = when (visuals.type) {
        SnackbarType.SUCCESS -> Color(0xFF2E7D32) to Icons.Default.CheckCircle
        SnackbarType.ERROR -> Color(0xFFC62828) to Icons.Default.Error
        SnackbarType.WARNING -> Color(0xFFF9A825) to Icons.Default.Warning
        SnackbarType.INFO -> Color(0xFF1565C0) to Icons.Default.Info
    }

    Box (modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.Center){
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor),
            shape = CardDefaults.elevatedShape,
            elevation = CardDefaults.elevatedCardElevation(4.dp)


        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = visuals.message,
                    color = Color.White
                )
            }
        }
    }
}


enum class SnackbarType {
    SUCCESS, ERROR, INFO, WARNING
}


data class CustomSnackbarVisuals(
    override val message: String,
    val type: SnackbarType,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

