package org.megamind.mycashpoint.ui.component

// Imports essentiels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable

/**
 * Boîte de dialogue de confirmation réutilisable.
 *
 * @param visible affiche ou non la dialog
 * @param title titre de la dialog
 * @param message message de la dialog
 * @param confirmText texte du bouton confirmer
 * @param dismissText texte du bouton annuler
 * @param onConfirm callback confirmation
 * @param onDismiss callback annulation / fermeture
 */
@Composable
fun ConfirmDialog(
    visible: Boolean,
    title: String = "Confirmer",
    message: String = "Êtes-vous sûr(e) ?",
    confirmText: String = "Confirmer",
    dismissText: String = "Annuler",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            // Bouton confirmation en rouge (adapter la couleur si nécessaire)
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissText)
            }
        }
    )
}
