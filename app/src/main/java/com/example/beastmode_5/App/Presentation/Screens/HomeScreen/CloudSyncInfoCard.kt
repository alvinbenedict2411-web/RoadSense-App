package com.example.beastmode_5.App.Presentation.Screens.HomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor

@Composable
fun CloudSyncInfoCard() {

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.CloudSync,
                    contentDescription = "Cloud Sync",
                    modifier = Modifier.size(24.dp),
                    tint = AccentBlue
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "How Cloud Sync Works",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomePrimaryText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "RoadSense stores detected potholes locally " +
                        "and allows you to sync reports to your cloud account.",
                fontSize = 14.sp,
                lineHeight = 19.sp,
                color = HomeSecondaryText,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(12.dp))

            CloudSyncPoint(
                text = "New reports are uploaded to your Firebase cloud account."
            )

            Spacer(modifier = Modifier.height(8.dp))

            CloudSyncPoint(
                text = "Before uploading, RoadSense checks for existing " +
                        "cloud potholes within 5 metres."
            )

            Spacer(modifier = Modifier.height(8.dp))

            CloudSyncPoint(
                text = "If a nearby cloud pothole is found for your account, " +
                        "that report is skipped during sync."
            )

            Spacer(modifier = Modifier.height(8.dp))

            CloudSyncPoint(
                text = "This helps reduce duplicate reports in the cloud."
            )
        }
    }
}