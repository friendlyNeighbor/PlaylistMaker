package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.playlistmaker.R


@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val LightColors = Colors(
        primary = colorResource(id = R.color.yp_black),
        primaryVariant = Color.Gray,          // аналог secondary/darker variant
        secondary = Color.LightGray,
        secondaryVariant = Color.DarkGray,
        background = colorResource(id = R.color.white),
        surface = colorResource(id = R.color.white),
        error = Color.Red,
        onPrimary = colorResource(id = R.color.white),
        onSecondary = Color.Black,
        onBackground = colorResource(id = R.color.yp_black),
        onSurface = colorResource(id = R.color.yp_black),
        onError = Color.White,
        isLight = true
    )

    val DarkColors = Colors(
        primary = colorResource(id = R.color.yp_black),
        primaryVariant = Color.DarkGray,
        secondary = Color.Black,
        secondaryVariant = Color.Black,
        background = Color.Black,
        surface = Color.DarkGray,
        error = Color.Red,
        onPrimary = Color.Red,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.Red,
        onError = Color.White,
        isLight = false
    )

    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colors = colors,
  //      typography = Typography,      // из material
  //      shapes = Shapes,               // из material
        content = content
    )
}
