package com.example.beastmode_5.App.Presentation.Screens.HomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecentDetectionsRow(pothole: Pothole,Clicked:()-> Unit){
    val formatter = SimpleDateFormat(
        "dd/MM/yyyy hh:mm a", Locale.getDefault())
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth(0.3f)
            .height(70.dp).clip(
                RoundedCornerShape(10.dp)
            )) {
            AsyncImage(model = pothole.image_path,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(text="${pothole.city_name}",
                fontSize = 14.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color=HomePrimaryText)
            Text(text="${pothole.area_name}",
                fontSize = 12.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
                color= HomeSecondaryText
            )
            Text(text="${formatter.format(Date(pothole.timestamp))}",
                fontSize = 12.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
                color= HomeSecondaryText
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = {Clicked()}) {
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = HomePrimaryText)
        }
        }

}