package com.example.customlayouts.layouts.lazy

import androidx.compose.runtime.Composable

data class ListItem(
    val index: Int,
    val x: Int,
    val y: Int
)

data class ViewBoundaries(
    val fromX: Int,
    val toX: Int,
    val fromY: Int,
    val toY: Int
)

typealias ComposableItemContent = @Composable (ListItem) -> Unit

data class LazyLayoutItemContent(
    val item: ListItem,
    val itemContent: ComposableItemContent
)
