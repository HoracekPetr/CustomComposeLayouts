package com.example.customlayouts.layouts.lazy

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

@Composable
fun rememberLazyLayoutState(): LazyLayoutState {
    return remember { LazyLayoutState() }
}

fun Modifier.lazyLayoutPointerInput(state: LazyLayoutState) = this.then(
    pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            state.onDrag(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
        }
    }
)

@Stable
class LazyLayoutState {
    private val _offsetState = mutableStateOf(IntOffset(0,0))
    val offsetState = _offsetState

    fun onDrag(offset: IntOffset) {
        val x = (offsetState.value.x - offset.x).coerceAtLeast(0)
        val y = (offsetState.value.y - offset.y).coerceAtLeast(0)
        _offsetState.value = IntOffset(x,y)
    }

    fun getBoundaries(
        constraints: Constraints,
        threshold: Int = 500
    ): ViewBoundaries {
        return ViewBoundaries(
            fromX = offsetState.value.x - threshold,
            toX = constraints.maxWidth + offsetState.value.x + threshold,
            fromY = offsetState.value.y - threshold,
            toY =  constraints.maxHeight + offsetState.value.y + threshold
        )
    }
}