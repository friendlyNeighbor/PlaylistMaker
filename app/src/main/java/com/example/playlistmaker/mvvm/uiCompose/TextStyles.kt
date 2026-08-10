package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TextStyles {
    @Composable
    fun headerStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp,
        color = MaterialTheme.colors.primary
    )
    @Composable
    fun actionPanelStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        color = MaterialTheme.colors.primary
    )

}