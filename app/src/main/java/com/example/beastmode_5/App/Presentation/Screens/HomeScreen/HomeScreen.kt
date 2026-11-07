package com.example.beastmode_5.App.Presentation.Screens.HomeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.beastmode_5.App.Presentation.Navigation.BeastMode5Routes
import com.example.beastmode_5.App.Presentation.Navigation.BottomNavigationBar
import com.example.beastmode_5.App.Presentation.Viewmodels.HomeScreenViewModel
import com.example.beastmode_5.R
import com.example.beastmode_5.ui.theme.HomeBackgroundColor
import com.example.beastmode_5.ui.theme.HomeBorderColor
import com.example.beastmode_5.ui.theme.HomeLightGreen
import com.example.beastmode_5.ui.theme.HomePrimaryBlue
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomePurple
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSuccessGreen
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewmodel: HomeScreenViewModel,navController: NavController) {
    val formatter = SimpleDateFormat(
        "dd/MM/yyyy hh:mm a",
        Locale.getDefault()
    )
    Scaffold(
        bottomBar = {
            BottomNavigationBar("Home",navController)
        }
    ) {
        innerpadding ->
    Box(
        modifier = Modifier.fillMaxSize()
            .background(HomeBackgroundColor)
            .padding(innerpadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon2),
                        contentDescription = "RoadSense Logo",
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.Center) {
                        Row() {
                            Text(
                                text = "Road",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = HomePrimaryText
                            )
                            Text(
                                text = "Sense",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = HomePrimaryBlue
                            )
                        }
                        Text(
                            text = "Safer Roads,Brighter Tomorrow",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = HomeSecondaryText
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(50.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            navController.navigate(BeastMode5Routes.UserDetailsScreen){
                                popUpTo(BeastMode5Routes.HomeScreen){
                                    inclusive = false
                                    saveState = true
                                }
                            }
                        }
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        if(viewmodel.IconUrl!=null){
                            SubcomposeAsyncImage(model=viewmodel.IconUrl,
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else{
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = HomePrimaryText,
                                modifier = Modifier.fillMaxSize()

                            )
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.imagebanner),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            item {
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Get Started",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = HomePrimaryBlue
                        )
                        Text(
                            text = "Detect potholes using your camera or analyze an existing pothole image",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Card(
                                modifier = Modifier.height(120.dp)
                                    .weight(1f)
                                    .clickable(onClick = {
                                        navController.navigate(BeastMode5Routes.LiveFeedScreen)
                                    }),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = HomePrimaryBlue
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Live Detection",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            text = "Start scanning ",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 10.sp,
                                            color = Color.White
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }

                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            Card(
                                modifier = Modifier.height(120.dp)
                                    .weight(1f)
                                    .clickable(onClick = {
                                        navController.navigate(BeastMode5Routes.StaticFeedScreen)
                                    }),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = HomePurple
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Image Detection",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center) {
                                        Text(
                                            text = "Choose image ",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 10.sp,
                                            color = Color.White
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
            item {
                if (viewmodel.totalpotholes == 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .height(135.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HomeSurfaceColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(10.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = null,
                                    tint = HomePrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Your Reports",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                        .background(HomePrimaryBlue.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .clickable(onClick = {
                                            navController.navigate(BeastMode5Routes.DetailsScreen){
                                                popUpTo(BeastMode5Routes.HomeScreen){
                                                    inclusive = true
                                                    saveState = true
                                                }
                                            }
                                        })
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "View All",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = HomePrimaryBlue
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = HomePrimaryBlue,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Reports Yet",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                            }

                        }
                    }
                } else {
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
                            modifier = Modifier.fillMaxWidth()
                                .padding(10.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = null,
                                    tint = HomePrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Your Reports",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =Modifier.clip(RoundedCornerShape(10.dp))
                                        .background(HomePrimaryBlue.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .clickable(onClick = {
                                            navController.navigate(BeastMode5Routes.DetailsScreen){
                                                popUpTo(BeastMode5Routes.HomeScreen){
                                                    inclusive = true
                                                    saveState = true
                                                }
                                            }
                                        })
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "View All",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = HomePrimaryBlue
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = HomePrimaryBlue,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)) {
                                    Text(
                                        viewmodel.totalpotholes.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = HomeSuccessGreen
                                    )
                                    Text(
                                        text = "Potholes Detected",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = HomeSecondaryText,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    thickness = 2.dp,
                                    color = HomeBorderColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)) {
                                    Text(
                                        viewmodel.totalareas.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = HomeSuccessGreen
                                    )
                                    Text(
                                        text = "Areas Covered",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = HomeSecondaryText,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    thickness = 2.dp,
                                    color = HomeBorderColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)) {
                                    Text(
                                        viewmodel.potholesdetectedtoday.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = HomeSuccessGreen
                                    )
                                    Text(
                                        text = "Today's Reports",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = HomeSecondaryText,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = HomeBorderColor)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = HomePrimaryBlue,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        "Last Detected",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = HomeSecondaryText
                                    )
                                    Text(
                                        "${viewmodel.lastdetectedareaname}, ${viewmodel.lastdetectedcityname}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = HomePrimaryText
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                val date = formatter.format(Date(viewmodel.lastpotholedetectedtime))
                                Text(
                                    date,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = HomeSecondaryText,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            item {
                if (viewmodel.totalpotholes == 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .height(135.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HomeSurfaceColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Column(modifier = Modifier.fillMaxSize()
                            .padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = HomePrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Recent Detections",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                                        .background(HomePrimaryBlue.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .clickable(onClick = {
                                        navController.navigate(BeastMode5Routes.DetailsScreen){
                                            popUpTo(BeastMode5Routes.HomeScreen) {
                                                inclusive = true
                                                saveState = true
                                            }
                                        }
                                    })
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "View All",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = HomePrimaryBlue
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = HomePrimaryBlue,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }

                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Recent Detections",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                            }

                        }
                    }
                } else {
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
                        Column(modifier = Modifier.fillMaxWidth()
                            .padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = HomePrimaryBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Recent Detections",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = HomePrimaryText
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier =Modifier.clip(RoundedCornerShape(10.dp))
                                        .background(HomePrimaryBlue.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .clickable(onClick = {
                                        navController.navigate(BeastMode5Routes.DetailsScreen) {
                                            popUpTo(BeastMode5Routes.HomeScreen) {
                                                inclusive = true
                                                saveState = true
                                            }
                                        }
                                    })
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "View All",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = HomePrimaryBlue
                                        )
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = HomePrimaryBlue,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }

                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            viewmodel.recentupto3potholedetails.forEachIndexed { index, pothole ->
                                RecentDetectionsRow(pothole, Clicked = {
                                    //navigate
                                    navController.navigate(BeastMode5Routes.DetailsScreen){
                                        popUpTo(BeastMode5Routes.HomeScreen){
                                            inclusive = true
                                            saveState = true
                                        }
                                    }
                                })
                                if(index<viewmodel.recentupto3potholedetails.size-1) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = HomeBorderColor)
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }
                        }

                    }
                }

            }
            item {
                HowRoadSenseWorks()
            }
            item {
                WhyRoadSenseCard()
            }
            item{
                CloudSyncInfoCard()
            }
            item {
                RoadSenseTipCard()
            }
        }
    }
}
}