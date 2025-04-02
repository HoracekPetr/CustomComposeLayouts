package com.example.customlayouts.layouts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.example.customlayouts.DEBUG_TAG

@Composable
fun CustomColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map {
            it.measure(constraints)
        }

        Log.d(DEBUG_TAG, "Constraints: $constraints")

        val width = placeables.maxOf { it.width }
        val height = placeables.sumOf { it.height }

        layout(width, height) {
            var y = 0
            placeables.forEach {
                it.placeRelative(x = 0, y = y)
                y += it.height
            }
        }
    }
}
