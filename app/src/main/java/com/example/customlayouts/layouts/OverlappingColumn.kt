package com.example.customlayouts.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp

@Composable
fun OverlappingColumn(modifier: Modifier = Modifier, overlap: Dp, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map {
            it.measure(constraints)
        }

        val totalHeight = placeables.sumOf { it.height - (overlap.roundToPx() / 2) }

        layout(constraints.maxWidth, totalHeight) {
            var y = 0

            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = y)
                y += (placeable.height - overlap.roundToPx())
            }
        }
    }
}