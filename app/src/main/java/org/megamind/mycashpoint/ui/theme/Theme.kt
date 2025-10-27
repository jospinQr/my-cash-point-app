package org.megamind.mycashpoint.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    // Couleurs principales
    primary = CashPointRed70,
    onPrimary = CashPointRed10,
    primaryContainer = CashPointRed30,
    onPrimaryContainer = CashPointRed90,

    // Couleurs secondaires
    secondary = CashPointBlue70,
    onSecondary = CashPointBlue10,
    secondaryContainer = CashPointBlue30,
    onSecondaryContainer = CashPointBlue90,

    // Couleurs tertiaires
    tertiary = AlertOrange70,
    onTertiary = AlertOrange10,
    tertiaryContainer = AlertOrange30,
    onTertiaryContainer = AlertOrange90,

    // Arrière-plans et surfaces
    background = NeutralGrey10,
    onBackground = NeutralGrey90,
    surface = NeutralGrey10,
    onSurface = NeutralGrey90,
    surfaceVariant = NeutralGrey20,
    onSurfaceVariant = NeutralGrey80,
    surfaceTint = CashPointRed70,

    // Conteneurs de surface
    surfaceContainer = NeutralGrey15,
    surfaceContainerHigh = NeutralGrey20,
    surfaceContainerHighest = NeutralGrey30,
    surfaceContainerLow = NeutralGrey10,
    surfaceContainerLowest = Color(0xFF0F1113),

    // Inverse (pour les éléments contrastants)
    inverseSurface = NeutralGrey90,
    inverseOnSurface = NeutralGrey20,
    inversePrimary = CashPointRed40,

    // Erreurs
    error = EmergencyCorail80,
    onError = EmergencyCorail20,
    errorContainer = EmergencyCorail30,
    onErrorContainer = EmergencyCorail90,

    // Contours
    outline = NeutralGrey60,
    outlineVariant = NeutralGrey30,

    // Scrim (overlays)
    scrim = Color.Black,
)

private val LightColorScheme = lightColorScheme(
    // Couleurs principales
    primary = CashPointRed50,
    onPrimary = Color.White,
    primaryContainer = CashPointRed90,
    onPrimaryContainer = CashPointRed10,

    // Couleurs secondaires
    secondary = CashPointBlue50,
    onSecondary = Color.White,
    secondaryContainer = CashPointBlue90,
    onSecondaryContainer = CashPointBlue10,

    // Couleurs tertiaires
    tertiary = AlertOrange50,
    onTertiary = Color.White,
    tertiaryContainer = AlertOrange90,
    onTertiaryContainer = AlertOrange10,

    // Arrière-plans et surfaces
    background = NeutralGrey99,
    onBackground = NeutralGrey10,
    surface = NeutralGrey99,
    onSurface = NeutralGrey10,
    surfaceVariant = NeutralGrey90,
    onSurfaceVariant = NeutralGrey30,
    surfaceTint = CashPointRed50,

    // Conteneurs de surface
    surfaceContainer = NeutralGrey94,
    surfaceContainerHigh = NeutralGrey90,
    surfaceContainerHighest = NeutralGrey85,
    surfaceContainerLow = NeutralGrey96,
    surfaceContainerLowest = Color.White,

    // Inverse (pour les éléments contrastants)
    inverseSurface = NeutralGrey20,
    inverseOnSurface = NeutralGrey95,
    inversePrimary = CashPointRed80,

    // Erreurs
    error = EmergencyCorail50,
    onError = Color.White,
    errorContainer = EmergencyCorail90,
    onErrorContainer = EmergencyCorail10,

    // Contours
    outline = NeutralGrey50,
    outlineVariant = NeutralGrey80,

    // Scrim (overlays)
    scrim = Color.Black,
)

@Composable
fun MyCashPointTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}