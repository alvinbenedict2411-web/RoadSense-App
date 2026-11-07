package com.example.beastmode_5.App.Presentation.Classes

import android.graphics.RectF

data class DetectionBox(
    val box: RectF,
    val score: Float
)
