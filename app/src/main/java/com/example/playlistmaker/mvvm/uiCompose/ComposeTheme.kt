package com.example.playlistmaker.mvvm.uiCompose

import android.annotation.SuppressLint
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.playlistmaker.R


@SuppressLint("ConflictingOnColor")
@Composable
fun ComposeTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val LightColors = Colors(
        primary = Color(0xFFFFFFFF),
        onPrimary = Color(0xFF1A1B22),



        primaryVariant = colorResource(id = R.color.test3),          // аналог secondary/darker variant
        secondary = colorResource(id = R.color.test3),
        secondaryVariant = colorResource(id = R.color.test3),
        background = colorResource(id = R.color.test3),
        surface = colorResource(id = R.color.test3),
        error = colorResource(id = R.color.test3),
        onSecondary = colorResource(id = R.color.test3),
        onBackground = colorResource(id = R.color.test3),
        onSurface = colorResource(id = R.color.test3),
        onError = colorResource(id = R.color.test3),
        isLight = true
    )

    val DarkColors = Colors(
        primary = Color(0xFF1A1B22),
        onPrimary = Color(0xFFFFFFFF),



        primaryVariant = colorResource(id = R.color.test2),
        secondary = colorResource(id = R.color.test2),
        secondaryVariant = colorResource(id = R.color.test2),
        background = colorResource(id = R.color.test2),
        surface = colorResource(id = R.color.test2),
        error = colorResource(id = R.color.test2),
        onSecondary = colorResource(id = R.color.test2),
        onBackground = colorResource(id = R.color.test2),
        onSurface = colorResource(id = R.color.test2),
        onError = colorResource(id = R.color.test2),
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
