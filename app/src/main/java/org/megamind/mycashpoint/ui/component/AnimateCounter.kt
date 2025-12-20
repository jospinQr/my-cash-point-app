package org.megamind.mycashpoint.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
) {
    val countString = count.toString()

    Row(modifier = modifier) {
        // Afficher tous les chiffres sauf le dernier normalement
        countString.dropLast(1).forEach { char ->
            Text(
                text = char.toString(),
                style = style
            )
        }

        // Animer uniquement le dernier chiffre
        AnimatedContent(
            targetState = countString.last(),
            transitionSpec = {
                // Slide up + fade pour le nouveau chiffre
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            },
            label = "counter_animation"
        ) { targetChar ->
            Text(
                text = targetChar.toString(),
                style = style
            )
        }
    }
}