package org.megamind.mycashpoint.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val ChashPointGreen10 = Color(0xFF002114)
val ChashPointGreen20 = Color(0xFF003A2A)
val ChashPointGreen30 = Color(0xFF005140)
val ChashPointGreen40 = Color(0xFF006B56)
val ChashPointGreen50 = Color(0xFF00855D) // Vert principal
val ChashPointGreen60 = Color(0xFF26A085)
val ChashPointGreen70 = Color(0xFF4DB6A0)
val ChashPointGreen80 = Color(0xFF70CCBB)
val ChashPointGreen90 = Color(0xFF96E2D6)
val ChashPointGreen95 = Color(0xFFC4F1EA)
val ChashPointGreen99 = Color(0xFFF0FDF9)

// Bleus (informations, navigation)
val ChashPointBlue10 = Color(0xFF001D36)
val ChashPointBlue20 = Color(0xFF003258)
val ChashPointBlue30 = Color(0xFF004B7A)
val ChashPointBlue40 = Color(0xFF00659E)
val ChashPointBlue50 = Color(0xFF0080C4)
val ChashPointBlue60 = Color(0xFF2E96D1)
val ChashPointBlue70 = Color(0xFF5AAEDD)
val ChashPointBlue80 = Color(0xFF85C5E9)
val ChashPointBlue90 = Color(0xFFB1DCF4)
val ChashPointBlue95 = Color(0xFFD8EEFA)

// Oranges (alertes, actions importantes)
val AlertOrange10 = Color(0xFF2D1600)
val AlertOrange20 = Color(0xFF4A2800)
val AlertOrange30 = Color(0xFF6A3B00)
val AlertOrange40 = Color(0xFF8C4F00)
val AlertOrange50 = Color(0xFF4B50) // Orange principal
val AlertOrange60 = Color(0xFFD87C00)
val AlertOrange70 = Color(0xFFE69429)
val AlertOrange80 = Color(0xFFF2AC52)
val AlertOrange90 = Color(0xFFFCC37C)
val AlertOrange95 = Color(0xFFFFE0A6)

// Rouges (erreurs, urgences)
val EmergencyRed10 = Color(0xFF410E0B)
val EmergencyRed20 = Color(0xFF601410)
val EmergencyRed30 = Color(0xFF8C1D18)
val EmergencyRed40 = Color(0xFFB3261E)
val EmergencyRed50 = Color(0xFFDC362E)
val EmergencyRed60 = Color(0xFFE46962)
val EmergencyRed70 = Color(0xFFEC928E)
val EmergencyRed80 = Color(0xFFF2B8B5)
val EmergencyRed90 = Color(0xFFF9DEDC)
val EmergencyRed95 = Color(0xFFFCEEEE)

// Neutres (texte, arrière-plans)
val NeutralGrey10 = Color(0xFF191C1D)
val NeutralGrey20 = Color(0xFF2F3133)
val NeutralGrey30 = Color(0xFF46474A)
val NeutralGrey40 = Color(0xFF5E5E62)
val NeutralGrey50 = Color(0xFF77767A)
val NeutralGrey60 = Color(0xFF918F94)
val NeutralGrey70 = Color(0xFFACA9AE)
val NeutralGrey80 = Color(0xFFC8C5CA)
val NeutralGrey90 = Color(0xFFE4E1E6)
val NeutralGrey95 = Color(0xFFF2F0F4)
val NeutralGrey99 = Color(0xFFFFFBFF)

// ========================================
// COULEURS SÉMANTIQUES SPÉCIFIQUES PHARMACIE
// ========================================

// Succès (médicaments disponibles, validation)
val SuccessGreen = ChashPointGreen60
val SuccessGreenContainer = ChashPointGreen90

// Attention (stock faible, avertissements)
val WarningAmber = AlertOrange60
val WarningAmberContainer = AlertOrange90

// Erreur (rupture de stock, erreurs système)
val ErrorRed = EmergencyRed50
val ErrorRedContainer = EmergencyRed90

// Information (conseils, aide)
val InfoBlue = ChashPointBlue60
val InfoBlueContainer = ChashPointBlue90

// Prescription (ordonnances)
val PrescriptionPurple = Color(0xFF6750A4)
val PrescriptionPurpleContainer = Color(0xFFEADDFF)

// ========================================
// SCHÉMAS DE COULEURS MATERIAL 3
// ========================================

val ChashPointDarkColorScheme = darkColorScheme(
    // Couleurs principales
    primary = ChashPointGreen80,
    onPrimary = ChashPointGreen10,
    primaryContainer = ChashPointGreen30,
    onPrimaryContainer = ChashPointGreen99,

    // Couleurs secondaires (informations)
    secondary = ChashPointBlue80,
    onSecondary = ChashPointBlue10,
    secondaryContainer = ChashPointBlue30,
    onSecondaryContainer = ChashPointBlue90,

    // Couleurs tertiaires (actions importantes)
    tertiary = AlertOrange80,
    onTertiary = AlertOrange10,
    tertiaryContainer = AlertOrange30,
    onTertiaryContainer = AlertOrange90,

    // Arrière-plans
    background = NeutralGrey10,
    onBackground = NeutralGrey90,
    surface = NeutralGrey10,
    onSurface = NeutralGrey90,
    surfaceVariant = NeutralGrey20,
    onSurfaceVariant = NeutralGrey80,

    // États d'erreur
    error = EmergencyRed80,
    onError = EmergencyRed10,
    errorContainer = EmergencyRed30,
    onErrorContainer = EmergencyRed90,

    // Contours et diviseurs
    outline = NeutralGrey60,
    outlineVariant = NeutralGrey30,

    // Surface containers
    surfaceContainer = NeutralGrey20,
    surfaceContainerHigh = NeutralGrey30,
    surfaceContainerHighest = NeutralGrey40,
    surfaceContainerLow = NeutralGrey10,
    surfaceContainerLowest = Color.Transparent,
)

val ChashPointLightColorScheme = lightColorScheme(
    // Couleurs principales
    primary = ChashPointGreen50,
    onPrimary = Color.White,
    primaryContainer = ChashPointGreen90,
    onPrimaryContainer = ChashPointGreen99,

    // Couleurs secondaires (informations)
    secondary = ChashPointBlue50,
    onSecondary = Color.White,
    secondaryContainer = ChashPointBlue90,
    onSecondaryContainer = ChashPointBlue10,

    // Couleurs tertiaires (actions importantes)
    tertiary = AlertOrange50,
    onTertiary = Color.White,
    tertiaryContainer = AlertOrange90,
    onTertiaryContainer = AlertOrange10,

    // Arrière-plans
    background = NeutralGrey99,
    onBackground = NeutralGrey10,
    surface = NeutralGrey99,
    onSurface = NeutralGrey10,
    surfaceVariant = NeutralGrey90,
    onSurfaceVariant = NeutralGrey30,

    // États d'erreur
    error = EmergencyRed50,
    onError = Color.White,
    errorContainer = EmergencyRed90,
    onErrorContainer = EmergencyRed10,

    // Contours et diviseurs
    outline = NeutralGrey50,
    outlineVariant = NeutralGrey80,

    // Surface containers
    surfaceContainer = NeutralGrey95,
    surfaceContainerHigh = NeutralGrey90,
    surfaceContainerHighest = NeutralGrey80,
    surfaceContainerLow = NeutralGrey99,
    surfaceContainerLowest = Color.White,
)