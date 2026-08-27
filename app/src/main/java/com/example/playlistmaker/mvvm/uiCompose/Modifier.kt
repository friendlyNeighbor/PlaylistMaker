package com.example.playlistmaker.mvvm.uiCompose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 0.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp
): Modifier {
    return this.drawBehind {
        drawRoundRect(
            color = color,
            size = size,
            cornerRadius = CornerRadius(
                cornerRadius.toPx(),
                cornerRadius.toPx()
            ),
            style = Stroke(
                width = strokeWidth.toPx(),
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(
                        dashLength.toPx(),
                        gapLength.toPx()
                    ),
                    phase = 0f
                )
            )
        )
    }
}