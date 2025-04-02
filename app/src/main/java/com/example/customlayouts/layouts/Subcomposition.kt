package com.example.customlayouts.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout

@Composable
fun HeaderContentLayout(
    modifier: Modifier = Modifier,
    header: @Composable (height: Int) -> Unit,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->

        val contentPlaceables = subcompose("content") { content() }
            .map { it.measure(constraints) }

        val contentHeightPx = contentPlaceables.sumOf { it.height }

        val headerHeightPx = contentHeightPx / 2

        val headerPlaceables = subcompose("header") { header(headerHeightPx) }
            .map { it.measure(constraints.copy(maxHeight = headerHeightPx)) }

        layout(constraints.maxWidth, contentHeightPx + headerHeightPx) {
            var y = 0

            headerPlaceables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }

            contentPlaceables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    }
}

/*
HeaderContentLayout(
header = { height ->
    Text(
        modifier = Modifier
            .height(height.dp)
            .debugBorder(), text = "Hlavička"
    )
},
content = {
    Column(
        modifier = Modifier.debugBorder(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(10) {
            Text("Obsah $it")
        }
    }
}
)*/
