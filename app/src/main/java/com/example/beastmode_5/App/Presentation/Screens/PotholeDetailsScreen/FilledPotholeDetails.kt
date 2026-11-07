package com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.InfoBannerColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText
import com.google.android.gms.maps.model.LatLng

@Composable
fun FilledPotholeDetails(
    potholedetails: List<Pothole>,
    allareas: List<String>,
    selectedarea: String,
    ChangeArea: (String) -> Unit,
    deleteclick: (Int, String) -> Unit,
    CoordinatesClick: (LatLng) -> Unit,
    onClickSync: () -> Unit,
    syncButtonEnabled: Boolean,
    onClickSwap: () -> Unit
) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(bottom = 10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Assignment,
                        contentDescription = "Pothole Details",
                        tint = AccentBlue,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Pothole Details",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomePrimaryText
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onClickSync()
                    },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        enabled = syncButtonEnabled,
                        modifier = Modifier.size(30.dp)
                        ) {
                        Icon(imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = {
                        onClickSwap()
                    },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Swap",
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(InfoBannerColor)
                        .padding(vertical = 12.dp,horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.clip(CircleShape)
                            .background(AccentBlue)
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column() {
                        Text(
                            text = "Pothole Reports",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = HomePrimaryText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "View detected potholes in your areas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = HomeSecondaryText
                        )
                    }
                }
            }

            item {
                FilterChipScreen(selectedarea, allareas, onFilterClick = { areaname ->
                    ChangeArea(areaname)
                })
            }
            if(selectedarea!="All" && potholedetails.size>=3){
                item {
                    PotholeAreaInfoCard(
                        areaName = selectedarea,
                        potholeCount = potholedetails.size
                    )
                }
            }
            items(potholedetails) { pothole ->
                PotholeCard(
                    pothole,
                    ondeleteclick = {
                        deleteclick(pothole.id,pothole.area_name)
                    },
                    onCoordinatesClick = {
                        val locationforzoom= LatLng(
                            pothole.latitude,
                            pothole.longitude
                        )
                        CoordinatesClick(locationforzoom)
                    })
            }
        }
    }