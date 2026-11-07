package com.example.beastmode_5.App.Presentation.Screens.UserDetailsScreen

import android.R
import android.R.attr.color
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserDetailFilled(
    username: String,
    usernameforchange: String,
    useremail: String,
    userIcon: Uri?,
    allpotholesfromroomCount: Int,
    allareasfromroomCount: Int,
    allpotholesfromcloudCount: Int,
    allareasfromcloud: Int,
    totalreportsinroom: Int,
    waitingtoSync: Int,
    cloudStatus: Boolean,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onClickRefresh:()-> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        var isRefreshing by remember { mutableStateOf(false) }
        val scope= rememberCoroutineScope()
        val state= rememberPullToRefreshState()
        PullToRefreshBox(
        isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing=true
                    onClickRefresh()
                    delay(1000)
                    isRefreshing=false
                }
            },
            state = state,
            indicator = {
                Indicator(
                    state = state,
                    isRefreshing = isRefreshing,
                    containerColor = Color.White,
                    color = Color(0xFF3568C0),
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(top=10.dp,
                    bottom=10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    UserProfileHeader(username, useremail, userIcon, onClickEdit = {
                        onEditClick()
                    })
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
                        Column(modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)){
                            Row(modifier = Modifier.fillMaxWidth()
                                .height(IntrinsicSize.Min),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)){
                                CardDetailsStat("Reports on Device + Cloud", Icons.Default.Report,allpotholesfromroomCount+allpotholesfromcloudCount,"Total Reports in Room And Cloud Servers",
                                    Color(0xFF3568C0),modifier = Modifier.weight(1f))

                                CardDetailsStat(
                                    title = "Room Reports",
                                    counts = allpotholesfromroomCount,
                                    description = "Total Reports in Room DataBase",
                                    icon = Icons.Default.LocationOn,
                                    color=Color(0xFF16865B),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()
                                .height(IntrinsicSize.Min),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)){
                                CardDetailsStat("Cloud Reports", Icons.Default.Cloud,allpotholesfromcloudCount,"Reports in Firebase",
                                    Color(0xFF8B5CC7),modifier = Modifier.weight(1f))

                                CardDetailsStat(
                                    title = "Areas Covered",
                                    counts = allareasfromcloud+allareasfromroomCount,
                                    description = "Total Areas Covered In Room And Cloud Servers",
                                    icon =  Icons.Default.Storage,
                                    color= Color(0xFFE18A32),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                item{
                    CloudActivitySection(allpotholesfromroomCount,allpotholesfromcloudCount,waitingtoSync,cloudStatus)
                }
                item {
                    AccountSection(
                        onEditClick = {
                            onEditClick()
                        },
                        onLogoutClick = {
                            onLogoutClick()
                        }
                    )
                }
            }
        }

    }
}