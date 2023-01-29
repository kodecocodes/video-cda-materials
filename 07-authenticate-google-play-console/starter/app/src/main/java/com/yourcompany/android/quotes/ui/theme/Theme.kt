package com.yourcompany.android.quotes.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val Colors = lightColors(
  primary = md_theme_light_primary,
  onPrimary = md_theme_light_onPrimary,
  secondary = md_theme_light_secondary,
  onSecondary = md_theme_light_onSecondary,
  background = md_theme_light_background,
  onBackground = md_theme_light_onBackground,
  surface = md_theme_light_surface,
  onSurface = md_theme_light_onSurface,
)

@Composable
fun QuotesTheme(content: @Composable () -> Unit) {

  MaterialTheme(
    colors = Colors,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}