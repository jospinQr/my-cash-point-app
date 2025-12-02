package org.megamind.mycashpoint.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> TextDropdown(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    getText: (T) -> String
) {
    Column(modifier) {
        // Champ cliquable avec bordure et icône
        Box(
            modifier = Modifier

                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onExpandedChange(true) }
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                // Affiche toujours le texte sélectionné
                Text(
                    text = selectedItem?.let { getText(it) } ?: "Sélectionner",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedItem == null) Color.Gray else MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }
        }

        // Menu déroulant
        DropdownMenu(
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 8.dp,
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(getText(item)) },
                    onClick = {
                        onItemSelected(item)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}
