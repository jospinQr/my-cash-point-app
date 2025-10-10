package org.megamind.mycashpoint.ui.component
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.megamind.mycashpoint.ui.theme.MyCashPointTheme

// Couleurs réutilisables
object AppColors {
    val Primary = Color(0xFF4CAF50) // Vert
    val Secondary = Color(0xFFFFC107) // Jaune
    val Accent = Color(0xFF1E88E5) // Bleu
}

@Composable
fun CashPointLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(100.dp)) {
        // Cercle extérieur (représente une pièce)
        drawCircle(
            color = AppColors.Primary,
            radius = size.minDimension / 2,
            center = center,
            style = Fill
        )

        // Cercle intérieur pour relief
        drawCircle(
            color = AppColors.Secondary,
            radius = size.minDimension / 2.5f,
            center = center,
            style = Fill
        )

        // Symbole "$" ou "C" pour CashPoint
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "$",
                center.x,
                center.y + 15, // Ajustement vertical
                android.graphics.Paint().apply {
                    color = AppColors.Accent.toArgb()
                    textSize = 40f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                }
            )
        }

        // Optionnel : petit rectangle ou carré pour donner un style "app" moderne
        drawRoundRect(
            color = AppColors.Accent,
            topLeft = Offset(size.width * 0.25f, size.height * 0.7f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.5f, size.height * 0.1f),
            cornerRadius = CornerRadius(10f, 10f),
            style = Fill
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CashPointLogoPreview() {
    MyCashPointTheme {
        CashPointLogo()
    }
}
