package com.example.playlistmaker.mvvm.uiCompose

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
        letterSpacing = 0.sp
    )

    @Composable
    fun placeholderStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 19.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    )

    @Composable
    fun panelStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    )

    @Composable
    fun additionalButtonStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    )

    @Composable
    fun buttonStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.sp
    )

    @Composable
    fun cardStyle() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    )

    @Composable
    fun panelStyleSmall() = TextStyle(
        fontFamily = displayFontFamily,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp
    )


}