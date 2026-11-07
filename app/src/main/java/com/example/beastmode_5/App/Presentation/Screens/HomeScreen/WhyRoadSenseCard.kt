package com.example.beastmode_5.App.Presentation.Screens.HomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.HomePrimaryBlue
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSuccessGreen
import com.example.beastmode_5.ui.theme.HomeSurfaceColor

@Composable
fun WhyRoadSenseCard() {
    Card(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = HomeSurfaceColor
        )
        ) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShieldMoon,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = HomePrimaryBlue
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Why Use RoadSense?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomePrimaryText
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(modifier = Modifier.weight(1f)
                    .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = HomeSuccessGreen.copy(alpha = 0.12f)
                    )
                    ){
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.AltRoute,
                            contentDescription = "Safer Roads",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF16866F)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Safer Roads",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Helps identify and\nreport road issues",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = HomeSecondaryText,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)
                    .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = HomePrimaryBlue.copy(alpha = 0.12f)
                    )
                ){
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Community Driven",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF2563EB)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Community Driven",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Contribute to\nbetter infrastructure",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Normal,
                            color = HomeSecondaryText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(modifier = Modifier.weight(1f)
                    .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp

                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF06423).copy(alpha = 0.12f)
                    )
                ){
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Real-Time Mapping",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFFF06423)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Real-Time Mapping",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "View pothole locations\non map",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = HomeSecondaryText,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)
                    .fillMaxHeight(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp

                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6D3FE8).copy(alpha = 0.12f)
                    )
                ){
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Data Sync",
                            modifier = Modifier.size(30.dp),
                            tint = Color(0xFF6D3FE8)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Data Sync",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your reports are safely\nstored in the cloud",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = HomeSecondaryText,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}