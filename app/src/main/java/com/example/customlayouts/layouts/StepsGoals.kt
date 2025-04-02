package com.example.customlayouts.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepsGoals(
    modifier: Modifier = Modifier,
    steps: List<Int>,
    daysPadding: Dp = 12.dp,
    interval: Int = 2000
) {
    val stepsForHeader by remember {
        mutableStateOf(getStepsForHeader(steps, interval))
    }

    val maxSteps = stepsForHeader.maxOrNull()

    val stepsByDay = steps.map {
        val result = it.toFloat() / (maxSteps ?: 1)
        result
    }

    val stepsHeader = @Composable {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stepsForHeader.forEach {
                Text(text = it.toString(), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            }
        }
    }

    val dayLabels = @Composable {
        listOf(
            "Mon",
            "Tue",
            "Wen",
            "Thu",
            "Fri",
            "Sat",
            "Sun"
        ).forEach { Text(modifier = Modifier.padding(daysPadding), text = it, style = TextStyle(fontSize = 20.sp)) }
    }

    val stepBars = @Composable {
        steps.forEach { _ ->
            Spacer(modifier = Modifier
                .drawWithCache {
                    onDrawBehind {
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(44, 193, 50, 255),
                                    Color(59, 255, 68, 255)
                                )
                            ),
                            cornerRadius = CornerRadius(12f)
                        )
                    }
                }
                .height(20.dp))
        }
    }

    Layout(
        modifier = modifier,
        contents = listOf(stepsHeader, dayLabels, stepBars)
    ) { (steps, days, bars), constraints ->

        val stepsMeasurable = steps.first().measure(constraints)
        val daysMeasurables = days.map { it.measure(constraints) }
        val barsMeasurables = bars.mapIndexed { index, measurable ->
            val barWidth = stepsByDay.getOrNull(index)?.times(stepsMeasurable.width) ?: 0
            measurable.measure(
                constraints.copy(
                    minWidth = barWidth.toInt(),
                    maxWidth = barWidth.toInt()
                )
            )
        }


        layout(constraints.maxWidth, constraints.maxHeight) {
            val x = daysMeasurables.first().width
            var y = stepsMeasurable.height
            val barX = daysMeasurables.maxOf { it.width }

            stepsMeasurable.placeRelative(x, 0)
            daysMeasurables.forEachIndexed { index, day ->
                day.placeRelative(0, y)
                val bar = barsMeasurables.getOrNull(index)
                bar?.placeRelative(barX, y + daysPadding.roundToPx())
                y += day.height
            }
        }
    }
}

private fun getStepsForHeader(steps: List<Int>, interval: Int): List<Int> {
    val min = (steps.minOrNull()?.div(interval))?.times(interval) ?: return emptyList()
    val max = ((steps.maxOrNull()?.plus(interval - 1)))
        ?.div(interval)
        ?.times(interval)
        ?: return emptyList()

    return (min..max step interval).toList()
}

/*
StepsGoals(
steps = listOf(1200,5000,3050,8000,4000, 10000, 6500)
)*/
