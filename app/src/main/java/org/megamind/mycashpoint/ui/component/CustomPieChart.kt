package org.megamind.mycashpoint.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min


// Données pour le pie chart
@Stable
data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color
)

/**
 * Un composable de graphique en secteurs (pie chart) simple et animé.
 */
@Composable
fun AnimatedPieChart(
    modifier: Modifier = Modifier,
    data: List<PieChartData>,
    animationDurationMillis: Int = 1500,
    showLegend: Boolean = true,
    centerText: String? = null
) {
    // Animation progress
    val animatedProgress = remember { Animatable(0f) }

    // Déclencher l'animation
    LaunchedEffect(data) {
        animatedProgress.animateTo(
            1f,
            animationSpec = tween(animationDurationMillis)
        )
    }

    // Calculer le total pour les pourcentages
    val total = data.sumOf { it.value.toDouble() }.toFloat()

    if (total == 0f) return

    if (showLegend) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pie Chart
            PieChartCanvas(
                modifier = Modifier.size(200.dp),
                data = data,
                total = total,
                animatedProgress = animatedProgress.value,
                centerText = centerText
            )

            // Legend
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Répartition",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                data.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Indicateur de couleur
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(item.color)
                        )

                        // Label et pourcentage
                        Column {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                            val percentage = ((item.value / total) * 100).toInt()
                            Text(
                                text = "${item.value.toInt()} ($percentage%)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    } else {
        PieChartCanvas(
            modifier = modifier.size(220.dp),
            data = data,
            total = total,
            animatedProgress = animatedProgress.value,
            centerText = centerText
        )
    }
}

@Composable
private fun PieChartCanvas(
    modifier: Modifier,
    data: List<PieChartData>,
    total: Float,
    animatedProgress: Float,
    centerText: String?
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = min(size.width, size.height)
            val radius = canvasSize / 2.5f
            val center = Offset(size.width / 2f, size.height / 2f)

            var startAngle = -90f // Commencer en haut

            // Dessiner chaque secteur
            data.forEach { item ->
                val angle = (item.value / total) * 360f
                val sweepAngle = angle * animatedProgress

                if (sweepAngle > 0) {
                    // Dessiner le secteur
                    drawArc(
                        color = item.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(
                            center.x - radius,
                            center.y - radius
                        ),
                        size = Size(radius * 2, radius * 2)
                    )

                    // Contour blanc
                    drawArc(
                        color = Color.White,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(
                            center.x - radius,
                            center.y - radius
                        ),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                startAngle += sweepAngle
            }

            // Dessiner le cercle central (effet donut)
            val innerRadius = radius * 0.45f
            drawCircle(
                color = Color.White,
                radius = innerRadius,
                center = center
            )
        }

        // Texte central
        centerText?.let { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}


fun getPieChartColor(index: Int): Color {
    val colors = listOf(
        Color(0xFFEF4444), // Red
        Color(0xFFF59E0B), // Amber
        Color(0xFF8B5A2B), // Brown
        Color(0xFF6B7280)  // Gray
    )
    return colors[index % colors.size]
}


// Modernized Animated Pie Chart with Improved UI/UX
// By ChatGPT UI/UX Expert


@Composable
fun ModernPieChart(
    modifier: Modifier = Modifier,
    data: List<PieChartData>,
    animationDuration: Int = 1400,
    centerText: String? = null,
    showLegend: Boolean = true
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(animationDuration)
        )
    }

    val total = data.sumOf { it.value.toDouble() }.toFloat()
    if (total == 0f) return

    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chart
        ModernPieChartCanvas(
            modifier = Modifier.size(230.dp),
            data = data,
            total = total,
            animatedProgress = animatedProgress.value,
            centerText = centerText
        )

        if (showLegend) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Répartition",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                data.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(item.color)
                        )

                        Column {
                            Text(item.label, fontWeight = FontWeight.SemiBold)
                            val percent = ((item.value / total) * 100).toInt()
                            Text(
                                "${item.value.toInt()} ($percent%)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernPieChartCanvas(
    modifier: Modifier,
    data: List<PieChartData>,
    total: Float,
    animatedProgress: Float,
    centerText: String?
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val sizeMin = min(size.width, size.height)
            val radius = sizeMin / 2.2f
            val center = Offset(size.width / 2, size.height / 2)
            var startAngle = -90f

            data.forEach { item ->
                val angle = (item.value / total) * 360f
                val sweepAngle = angle * animatedProgress

                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                startAngle += sweepAngle
            }

            // Inner circle
            drawCircle(
                color = Color.White,
                radius = radius * 0.5f,
                center = center,
                style = Stroke(width = 6f)
            )
        }

        centerText?.let {
            Text(
                it,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


fun dynamicPieColor(index: Int): Color {

    val palette = listOf(

        Color(0xFFE91E63), // Red
        Color(0xFFFF5722), // Blue
        Color(0xFF104EB9), // Green
        Color(0xFFADBAFF), // Amber

    )
    return palette[index % palette.size]
}
