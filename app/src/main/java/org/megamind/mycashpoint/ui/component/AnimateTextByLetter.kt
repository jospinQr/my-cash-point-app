package org.megamind.mycashpoint.ui.component


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun AnimatedTextByLetter(
    modifier: Modifier = Modifier,
    text: String,
    startAnimation: Boolean
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        text.forEachIndexed { index, letter ->
            AnimatedLetter(
                letter = letter.toString(),
                delay = index * 100L,
                startAnimation = startAnimation
            )
        }
    }
}

@Composable
fun AnimatedLetter(
    letter: String,
    delay: Long,
    startAnimation: Boolean
) {
    var letterVisible by remember { mutableStateOf(false) }

    // Démarrer l'animation de chaque lettre avec un délai
    LaunchedEffect(startAnimation) {
        if (startAnimation) {
            delay(delay)
            letterVisible = true
        }
    }

    // Animation de chaque lettre
    val letterAlpha by animateFloatAsState(
        targetValue = if (letterVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "letter_alpha"
    )

    val letterScale by animateFloatAsState(
        targetValue = if (letterVisible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "letter_scale"
    )

    Text(
        text = letter,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp),
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .scale(letterScale)
            .alpha(letterAlpha)
    )
}
