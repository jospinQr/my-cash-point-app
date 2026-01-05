package org.megamind.mycashpoint.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTopAppBar(
    title: String,
    isLoading: Boolean = false,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    // Slot pour du contenu personnalisé au centre (comme votre Dropdown)
    customTitleContent: @Composable (RowScope.() -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.shadow(4.dp), // Légère ombre pour le style
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        navigationIcon = navigationIcon,
        actions = actions,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    // Groupe de Skeletons stylisés
                    Row {
                        SkeletonBox(width = 80.dp)
                        Spacer(Modifier.width(8.dp))
                        SkeletonBox(width = 100.dp)
                    }
                } else {
                    // Si on a un contenu perso (Dropdown), on l'affiche, sinon titre simple
                    if (customTitleContent != null) {

                        customTitleContent()

                    } else {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun SkeletonBox(width: Dp) {
    SkeletonLoadingEffect(
        modifier = Modifier
            .size(width = width, height = 24.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}