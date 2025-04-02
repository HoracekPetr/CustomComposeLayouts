package com.example.customlayouts.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

@Composable
fun BarList(modifier: Modifier = Modifier) {

    val wideningModifier = Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(
            constraints.copy(
                minWidth = constraints.maxWidth + 64.dp.roundToPx(),
                maxWidth = constraints.maxWidth + 64.dp.roundToPx()
            )
        )
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = constraints.maxWidth + 64.dp.roundToPx(),
                            maxWidth = constraints.maxWidth + 64.dp.roundToPx()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
                .height(20.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Red)
        )


/*        repeat(5) {
            Box(
                modifier = Modifier
                    .ifThenElse(
                        condition = selected == it,
                        thenModifier = { wideningModifier },
                        elseModifier = { Modifier.fillMaxWidth() }
                    )
                    .height(20.dp)
                    .background(Color.Red)
                    .clickable {
                        selected = it
                    }
            )
        }*/
    }
}

@Composable
fun Modifier.ifThenElse(
    condition: Boolean,
    thenModifier: @Composable () -> Modifier,
    elseModifier: @Composable () -> Modifier
) = this.then(if (condition) thenModifier() else elseModifier())