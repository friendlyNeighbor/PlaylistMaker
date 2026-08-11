package com.example.playlistmaker.mvvm.uiCompose

import android.annotation.SuppressLint
//import androidx.compose.material.Colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
    val LightColorScheme =lightColorScheme(

        primary = Color(0xFFFFFFFF),
        onPrimary = Color(0xFF1A1B22),



      //  primaryVariant = colorResource(id = R.color.test3),          // аналог secondary/darker variant
        secondary = colorResource(id = R.color.test3),
    //    secondaryVariant = colorResource(id = R.color.test3),
        background = colorResource(id = R.color.test3),
        surface = colorResource(id = R.color.test3),
        error = colorResource(id = R.color.test3),
        onSecondary = colorResource(id = R.color.test3),
        onBackground = colorResource(id = R.color.test3),
        onSurface = colorResource(id = R.color.test3),
        onError = colorResource(id = R.color.test3),
       // isLight = true
    )

    val DarkColorScheme = darkColorScheme(
        primary = Color(0xFF1A1B22),
        onPrimary = Color(0xFFFFFFFF),



     //   primaryVariant = colorResource(id = R.color.test2),
        secondary = colorResource(id = R.color.test2),
     //   secondaryVariant = colorResource(id = R.color.test2),
        background = colorResource(id = R.color.test2),
        surface = colorResource(id = R.color.test2),
        error = colorResource(id = R.color.test2),
        onSecondary = colorResource(id = R.color.test2),
        onBackground = colorResource(id = R.color.test2),
        onSurface = colorResource(id = R.color.test2),
        onError = colorResource(id = R.color.test2),
     //   isLight = false
    )

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
  //      typography = Typography,      // из material
  //      shapes = Shapes,               // из material
        content = content
    )


}
