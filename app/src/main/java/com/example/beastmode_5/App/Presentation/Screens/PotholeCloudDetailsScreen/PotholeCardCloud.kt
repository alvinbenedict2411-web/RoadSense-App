package com.example.beastmode_5.App.Presentation.Screens.PotholeCloudDetailsScreen
import com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen.LoadingScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.CardColor
import com.example.beastmode_5.ui.theme.DarkBackground
import com.example.beastmode_5.ui.theme.DividerColor
import com.example.beastmode_5.ui.theme.LargeColor
import com.example.beastmode_5.ui.theme.MediumColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText
import com.example.beastmode_5.ui.theme.SmallColor
import java.text.SimpleDateFormat
import java.util.Date

import com.example.beastmode_5.ui.theme.HomeBorderColor
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase

@Composable
fun PotholeCloudCard(pothole: PotholeFireBase,
                     onCoordinatesClick:()->Unit){
    val dateformat = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )
    val timeformat = SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    )
    val date = dateformat.format(Date(pothole.timestamp))
    val time = timeformat.format(Date(pothole.timestamp))
    val confidence=String.format(
        Locale.getDefault(),
        "%.1f%%",
        pothole.confidence * 100
    )
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
        colors = cardColors(
            containerColor = HomeSurfaceColor,
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = pothole.image_path,
                    contentDescription = "Pothole Image",
                    loading = {
                        LoadingScreen()
                    },
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error loading image"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = AccentBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = pothole.area_name,
                                color = HomePrimaryText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = pothole.city_name,
                            color = HomeSecondaryText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                            .background(if (pothole.pothole_size == "Small") SmallColor else if (pothole.pothole_size == "Medium") MediumColor else LargeColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = pothole.pothole_size,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth()
                        .background(HomeBorderColor),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = "Pothole Type",
                        modifier = Modifier.size(24.dp),
                        tint = HomeSecondaryText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Pothole Size",
                        color = HomeSecondaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = pothole.pothole_size,
                        color = HomePrimaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Confidence",
                        modifier = Modifier.size(24.dp),
                        tint = HomeSecondaryText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Confidence",
                        color = HomeSecondaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = confidence,
                        color = HomePrimaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Location",
                        modifier = Modifier.size(24.dp),
                        tint = HomeSecondaryText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Coordinates",
                        color = HomeSecondaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        onCoordinatesClick()
                    }, contentPadding = PaddingValues(0.dp)) {  // used content padding to remove the default padding of the text button on the start and end which caused the extra spacing
                        Text(
                            text = "${pothole.latitude}, ${pothole.longitude}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentBlue,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        modifier = Modifier.size(24.dp),
                        tint = HomeSecondaryText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Detected",
                        color = HomeSecondaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${date} ${time}",
                        color = HomePrimaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(1.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DeviceThermostat,
                        contentDescription = "Status",
                        modifier = Modifier.size(24.dp),
                        tint = HomeSecondaryText
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Status",
                        color = HomeSecondaryText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text =pothole.status,
                        color = if(pothole.status=="Resolved") Color.Green else Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}