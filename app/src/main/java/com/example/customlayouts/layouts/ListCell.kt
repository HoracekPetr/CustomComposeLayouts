package com.example.customlayouts.layouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun ListCell(
    modifier: Modifier = Modifier,
    showDivider: Boolean,
    leading: @Composable () -> Unit,
    cellContent: @Composable () -> Unit,
) {
    val divider: @Composable () -> Unit = if (showDivider) {
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )
        }
    } else {
        {
            Spacer(modifier = Modifier)
        }
    }

    Layout(
        modifier = modifier,
        contents = listOf(leading, cellContent, divider)
    ) { (leadingMeasurables, cellContentMeasurables, dividerMeasurables), constraints ->
        val leadingPlaceables = leadingMeasurables.map {
            it.measure(constraints)
        }

        require(leadingPlaceables.size == 1) {
            Log.e("ListCell", "You can only use one leading composable")
        }

        val cellContentPlaceables = cellContentMeasurables.map {
            it.measure(constraints)
        }

        val dividerPlaceables =
            if (showDivider) dividerMeasurables.map { it.measure(constraints) } else emptyList()

        val totalHeight = max(leadingPlaceables.sumOf { it.height },
            cellContentPlaceables.sumOf { it.height }) + dividerPlaceables.sumOf { it.height }

        layout(constraints.maxWidth, totalHeight) {
            var leadingHeight = 0
            var cellY: Int
            var x = 0
            var y = 0

            leadingPlaceables.forEach { leadingPlaceable ->
                leadingPlaceable.placeRelative(x = 0, y = 0)
                x += leadingPlaceable.width
                leadingHeight = leadingPlaceable.height
            }

            cellContentPlaceables.forEach { cellContentPlaceable ->
                cellY = (leadingHeight / 2) - (cellContentPlaceable.height / 2)
                cellContentPlaceable.placeRelative(x, cellY)
                y += cellContentPlaceable.height
            }

            dividerPlaceables.forEach { dividerPlaceable ->
                dividerPlaceable.placeRelative(x, max(leadingHeight, y))
            }
        }
    }
}

/*
ListCell(
leading = {
    Image(
        modifier = Modifier
            .size(108.dp)
            .padding(12.dp),
        painter = painterResource(R.drawable.sideeye_cat),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
},
cellContent = {
    CustomColumn {
        Text("Petr", style = TextStyle(fontSize = 28.sp))
        Text("Svetr")
    }
},
showDivider = true
)*/
