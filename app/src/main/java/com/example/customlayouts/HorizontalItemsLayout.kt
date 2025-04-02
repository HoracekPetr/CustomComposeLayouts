package com.example.customlayouts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/**Layout which places 3 input items horizontally if they fit or only two (first and last) vertically**/
@Composable
fun HorizontalItemsLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        require(measurables.size in 1..3) {
            Log.e("HorizontalItemsLayout", "HorizontalItemsLayout requires max 3 items")
        }
        val placeables = measurables.map { it.measure(constraints) }

        val canPlaceHorizontally = placeables.sumOf { it.width } <= constraints.maxWidth
        val layoutHeight = placeables.sumOf { it.height }

        layout(
            width = constraints.maxWidth,
            height = layoutHeight
        ) {
            var x = 0
            var y = 0

            if (canPlaceHorizontally) {
                placeables.forEach { placeable ->
                    placeable.placeRelative(x, 0)
                    x += placeable.width
                }
            } else {
                val (first, last) = listOf(
                    placeables.firstOrNull(),
                    placeables.lastOrNull().takeIf { placeables.size > 1 }
                )
                first?.placeRelative(0, y)
                y += first?.height ?: 0
                last?.place(0, y)
            }
        }
    }
}