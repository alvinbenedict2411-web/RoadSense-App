package com.example.beastmode_5.App.Presentation.Screens.HomeScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.HomePrimaryBlue
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor

@Composable
fun HowRoadSenseWorks() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = HomeSurfaceColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "How RoadSense Works",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HomePrimaryText
            )
            Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    RoadSenseStep(
                        icon = Icons.Default.CameraAlt,
                        iconBackground = Color(0xFFE5F0FF),
                        iconColor = HomePrimaryBlue,
                        title = "1. Detect",
                        description = "Capture road images or use live\ncamera while driving."
                    )

                    RoadSenseStep(
                        icon = Icons.Default.Settings,
                        iconBackground = Color(0xFFE5F8EC),
                        iconColor = Color(0xFF15803D),
                        title = "2. Analyze",
                        description = "Our AI model (YOLOv8) detects\npotholes automatically."
                    )

                    RoadSenseStep(
                        icon = Icons.Default.LocationOn,
                        iconBackground = Color(0xFFFFE8ED),
                        iconColor = Color(0xFFEF3340),
                        title = "3. Locate",
                        description = "GPS identifies the exact location\nof each pothole."
                    )

                    RoadSenseStep(
                        icon = Icons.Default.CloudUpload,
                        iconBackground = Color(0xFFEDE8FF),
                        iconColor = Color(0xFF6D3FE8),
                        title = "4. Sync",
                        description = "Reports are saved locally and\nsynced to Firebase."
                    )
                }

        }
    }
}