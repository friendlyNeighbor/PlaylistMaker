package com.example.playlistmaker.mvvm.uiCompose

import android.annotation.SuppressLint
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

        primary = Color(0xFFFFFFFF),// yp_white
        onPrimary = Color(0xFF1A1B22),// yp_black
        secondary = Color(0xFFE6E8EB), // YP Light Gray
        onSecondary = Color(0xFF1A1B22),// yp_black
        onSurface = Color(0xFFAEAFB4), // YP Text Gray
        onSurfaceVariant = Color(0xFFAEAFB4), // YP Text Gray
        tertiary = Color(0xFF3772E7), // progress
        background = Color(0xFFFFFFFF),
        primaryFixed = Color(0xFFAEAFB4),
        onPrimaryFixed = Color(0xFFFFFFFF),

            //  primaryVariant = colorResource(id = R.color.test3),          // аналог secondary/darker variant
    //    secondaryVariant = colorResource(id = R.color.test3),

     //   surface = colorResource(id = R.color.test),
        error = colorResource(id = R.color.test3),
        onBackground = colorResource(id = R.color.test3),
        onError = colorResource(id = R.color.test3),
        surfaceContainer = colorResource(id = R.color.test3),


    )

    val DarkColorScheme = darkColorScheme(
        primary = Color(0xFF1A1B22),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFF1A1B22),
        onSurface = Color(0xFF1A1B22),
        onSurfaceVariant = Color(0xFFFFFFFF),
        tertiary = Color(0xFF3772E7), // progress
        background = Color(0xFF1A1B22),
        primaryFixed = Color(0xFFAEAFB4),
        onPrimaryFixed = Color(0xFFFFFFFF),

     //   primaryVariant = colorResource(id = R.color.test2),

     //   secondaryVariant = colorResource(id = R.color.test2),
     //   inverseSurface = colorResource(id = R.color.test2),
        surface = colorResource(id = R.color.test2),
        error = colorResource(id = R.color.test2),
        onBackground = colorResource(id = R.color.test2),

        onError = colorResource(id = R.color.test2),
    )

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
  //      typography = Typography,      // из material
  //      shapes = Shapes,               // из material
        content = content
    )
}
