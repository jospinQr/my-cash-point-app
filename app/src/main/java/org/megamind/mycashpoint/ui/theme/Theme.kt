package org.megamind.mycashpoint.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val DarkColorScheme = darkColorScheme(
    // Couleurs Primaires
    primary = Bordeaux80,              // Bordeaux clair pour bonne visibilité
    onPrimary = Bordeaux20,            // Texte sur primary
    primaryContainer = Bordeaux30,     // Container plus foncé
    onPrimaryContainer = Bordeaux90,   // Texte sur container

    // Couleurs Secondaires
    secondary = Taupe80,               // Taupe clair
    onSecondary = Taupe20,             // Texte sur secondary
    secondaryContainer = Taupe30,      // Container secondaire
    onSecondaryContainer = Taupe90,    // Texte sur container secondaire

    // Couleurs Tertiaires
    tertiary = Copper80,               // Cuivre clair
    onTertiary = Copper20,             // Texte sur tertiary
    tertiaryContainer = Copper30,      // Container tertiaire
    onTertiaryContainer = Copper90,    // Texte sur container tertiaire

    // Couleurs d'Erreur
    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,

    // Surfaces et Backgrounds
    background = Neutral10,            // Fond principal très sombre
    onBackground = Neutral90,          // Texte sur fond
    surface = Neutral10,               // Surface standard
    onSurface = Neutral90,             // Texte sur surface
    surfaceVariant = NeutralVariant30, // Surface avec variation
    onSurfaceVariant = NeutralVariant80, // Texte sur surface variant

    // Surfaces tonales
    surfaceTint = Bordeaux80,          // Teinte pour surfaces élevées
    inverseSurface = Neutral90,        // Surface inversée
    inverseOnSurface = Neutral20,      // Texte sur surface inversée
    inversePrimary = Bordeaux40,       // Primary inversé

    // Bordures et dividers
    outline = NeutralVariant60,        // Bordures standards
    outlineVariant = NeutralVariant30, // Bordures subtiles

    // Scrim pour overlays
    scrim = Color(0xFF000000)
)

// ============================================
// SCHÉMAS DE COULEURS - MODE CLAIR
// ============================================

private val LightColorScheme = lightColorScheme(
    // Couleurs Primaires
    primary = Bordeaux40,              // Bordeaux principal
    onPrimary = Color.White,           // Texte blanc sur primary
    primaryContainer = Bordeaux90,     // Container clair
    onPrimaryContainer = Bordeaux10,   // Texte foncé sur container

    // Couleurs Secondaires
    secondary = Taupe40,               // Taupe principal
    onSecondary = Color.White,         // Texte blanc sur secondary
    secondaryContainer = Taupe90,      // Container secondaire clair
    onSecondaryContainer = Taupe10,    // Texte foncé sur container

    // Couleurs Tertiaires
    tertiary = Copper40,               // Cuivre principal
    onTertiary = Color.White,          // Texte blanc sur tertiary
    tertiaryContainer = Copper90,      // Container tertiaire clair
    onTertiaryContainer = Copper10,    // Texte foncé sur container

    // Couleurs d'Erreur
    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error10,

    // Surfaces et Backgrounds
    background = Neutral99,            // Fond blanc cassé
    onBackground = Neutral10,          // Texte très foncé
    surface = Neutral99,               // Surface blanche
    onSurface = Neutral10,             // Texte sur surface
    surfaceVariant = NeutralVariant90, // Surface avec variation
    onSurfaceVariant = NeutralVariant30, // Texte sur surface variant

    // Surfaces tonales
    surfaceTint = Bordeaux40,          // Teinte pour élévation
    inverseSurface = Neutral20,        // Surface inversée (sombre)
    inverseOnSurface = Neutral95,      // Texte sur inversé
    inversePrimary = Bordeaux80,       // Primary inversé

    // Bordures et dividers
    outline = NeutralVariant50,        // Bordures visibles
    outlineVariant = NeutralVariant80, // Bordures subtiles

    // Scrim pour overlays
    scrim = Color(0xFF000000)
)


@Composable
fun MyCashPointTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            if (darkTheme) darkColorScheme() else lightColorScheme()
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}