package com.example.customlayouts.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StaircaseLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val height = placeables.sumOf { it.height }
        layout(constraints.maxWidth, height) {
            var x = 0
            var y = 0
            var leftToRight = true

            placeables.forEach { placeable ->
                placeable.placeRelative(x, y)

                if (leftToRight) {
                    if (x + placeable.width < constraints.maxWidth) {
                        x += placeable.width
                    } else {
                        x -= placeable.width
                        leftToRight = false
                    }
                } else {
                    if (x - placeable.width >= 0) {
                        x -= placeable.width
                    } else {
                        x += placeable.width
                        leftToRight = true
                    }
                }
                y += placeable.height
            }
        }
    }
}