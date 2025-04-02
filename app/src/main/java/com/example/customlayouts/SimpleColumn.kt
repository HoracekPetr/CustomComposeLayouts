package com.example.customlayouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun SimpleColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        val width = placeables.maxOf { it.width }
        val height = placeables.sumOf { it.height }

        layout(width, height) {
            var y = 0
            placeables.forEach {
                it.placeRelative(0,y)
                y += it.height
            }
        }
    }
}