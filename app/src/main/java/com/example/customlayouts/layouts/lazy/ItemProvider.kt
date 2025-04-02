package com.example.customlayouts.layouts.lazy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalFoundationApi::class)
class ItemProvider(
    private val itemsState: State<List<LazyLayoutItemContent>>
) : LazyLayoutItemProvider {
    override val itemCount: Int
        get() = itemsState.value.size

    @Composable
    override fun Item(index: Int, key: Any) {
        val item = itemsState.value.getOrNull(index)
        item?.itemContent?.invoke(item.item)
    }

    fun getItemIndexesInRange(boundaries: ViewBoundaries): List<Int> {
        return buildList {
            itemsState.value.forEachIndexed { index, itemContent ->
                val listItem = itemContent.item
                if (listItem.x in boundaries.fromX..boundaries.toX && listItem.y in boundaries.fromY..boundaries.toY) {
                    add(index)
                }
            }
        }
    }

    fun getItem(index: Int): ListItem? {
        return itemsState.value.getOrNull(index)?.item
    }
}

@Composable
fun rememberItemProvider(customLazyListScope: CustomLazyListScope.() -> Unit): ItemProvider {
    val customLazyListScopeState = remember { mutableStateOf(customLazyListScope) }.apply {
        value = customLazyListScope
    }

    return remember {
        ItemProvider(
            derivedStateOf {
                val layoutScope = CustomLazyListScopeImpl().apply(customLazyListScopeState.value)
                layoutScope.items
            }
        )
    }
}