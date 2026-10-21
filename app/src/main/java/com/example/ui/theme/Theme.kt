package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = ElectricBlue,
    onPrimary = GarageBackground,
    primaryContainer = GarageSurfaceVariant,
    onPrimaryContainer = ElectricBlue,
    secondary = NeonOrange,
    onSecondary = GarageBackground,
    secondaryContainer = GarageSurfaceVariant,
    onSecondaryContainer = NeonOrange,
    tertiary = StatHandlingColor,
    background = GarageBackground,
    onBackground = TextPrimary,
    surface = GarageSurface,
    onSurface = TextPrimary,
    surfaceVariant = GarageSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GarageSurfaceBorder
  )

private val LightColorScheme = DarkColorScheme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> DarkColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
