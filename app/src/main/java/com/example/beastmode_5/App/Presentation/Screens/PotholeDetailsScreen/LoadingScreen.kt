package com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.beastmode_5.ui.theme.AccentBlue

@Composable
fun LoadingScreen(){
    Box(modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AccentBlue)
    }
}