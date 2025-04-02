package com.example.customlayouts.layouts.lazycolumn

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.round
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleLazyColumn(
    modifier: Modifier = Modifier,
    itemCount: Int = 50,
    itemContent: @Composable (index: Int) -> Unit = {
        Log.d("SimpleLazyColumn", "Composing $it")
        Text(text = "Text $it")
    }
) {
    var scrollOffset by remember { mutableIntStateOf(0) }
    var maxScrollOffset by remember { mutableIntStateOf(0) }

    val itemProvider = remember {
        object : LazyLayoutItemProvider {
            override val itemCount: Int
                get() = itemCount

            @Composable
            override fun Item(index: Int, key: Any) {
                itemContent(index)
            }
        }
    }

    LazyLayout(
        modifier = modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollableState { delta ->
                val newScrollOffset = (scrollOffset - delta).roundToInt().coerceIn(0, maxScrollOffset)
                val consumed = newScrollOffset - scrollOffset
                scrollOffset = newScrollOffset
                consumed.toFloat()
            }
        ),
        itemProvider = { itemProvider }
    ) { constraints ->
        val viewport = Size(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
        val itemDimensions = measure(0, constraints).firstOrNull()?.run {
            Size(width.toFloat(), height.toFloat())
        } ?: Size.Zero

        val visibleItems = calculateVisibleItems(
            viewport = viewport,
            itemDimensions = itemDimensions,
            scrollOffset = scrollOffset,
            itemCount = itemCount,
        )

        val placeables = visibleItems.map { index ->
            measure(index, constraints)
        }

        maxScrollOffset = calculateMaxScrollOffset(itemCount, itemDimensions, viewport)

        layout(viewport.width.roundToInt(), viewport.height.roundToInt()) {
            var y = 0
            placeables.forEach { placeableList ->
                placeableList.forEach { placeable ->
                    placeable.placeRelative(0, y)
                    y += itemDimensions.height.roundToInt()
                }
            }
        }
    }
}

private fun calculateVisibleItems(
    viewport: Size,
    itemDimensions: Size,
    scrollOffset: Int,
    itemCount: Int,
): List<Int> {
    if (itemDimensions == Size.Zero) return emptyList()

    val firstVisibleItemIndex = (scrollOffset / itemDimensions.height).toInt()
    val numVisibleItems = (viewport.height / itemDimensions.height).toInt() + 1

    return (firstVisibleItemIndex until minOf(
        firstVisibleItemIndex + numVisibleItems,
        itemCount
    )).toList()
}

private fun calculateMaxScrollOffset(
    itemCount: Int,
    itemDimensions: Size,
    viewport: Size
): Int {
    if (itemDimensions == Size.Zero) return 0
    val totalHeight = itemCount * itemDimensions.height
    return (totalHeight - viewport.height).roundToInt().coerceAtLeast(0)
}