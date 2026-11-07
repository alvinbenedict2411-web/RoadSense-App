package com.example.beastmode_5.App.Presentation.Screens.UserDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor

@Composable
fun CloudActivitySection(
    allpotholesfromroomCount: Int,
    allpotholesfromcloudCount: Int,
    waitingtoSync: Int,
    cloudStatus: Boolean,
    modifier: Modifier = Modifier
) {
    val statusText = if (cloudStatus) "Connected" else "Unavailable"

    val statusColor = if (cloudStatus) {
        Color(0xFF16865B)
    } else {
        Color(0xFFD64545)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = Color(0xFF3568C0),
                    modifier = Modifier.size(28.dp)
                )

                Text(
                    text = "Cloud Activity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomePrimaryText
                )
            }

            CloudActivityRow(
                title = "Local Reports",
                count = allpotholesfromroomCount,
                icon = Icons.Default.CloudSync,
                iconColor = Color(0xFF3568C0)
            )

            CloudActivityRow(
                title = "Uploaded Reports",
                count = allpotholesfromcloudCount,
                icon = Icons.Default.Upload,
                iconColor = Color(0xFF16865B)
            )

            CloudActivityRow(
                title = "Waiting to Sync",
                count = waitingtoSync,
                icon = Icons.Default.CloudSync,
                iconColor = Color(0xFFE18A32)
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = if (cloudStatus) {
                            Icons.Default.Wifi
                        } else {
                            Icons.Default.CloudOff
                        },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(22.dp)
                    )

                    Text(
                        text = "Cloud Status",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = HomeSecondaryText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                color = statusColor,
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = statusText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor
                    )
                }
            }
        }
    }
}