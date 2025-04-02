package com.example.customlayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.customlayouts.ui.theme.CustomLayoutsTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomLayoutsTheme {
                Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    HorizontalItemsLayout {

                    }
                }
            }
        }
    }
}

const val DEBUG_TAG = "[CustomLayout]"

fun Modifier.debugBorder() =
    this.then(border(1.dp, getRandomColor()))

private fun getRandomColor() = Color(Random.nextInt(), Random.nextInt(), Random.nextInt())