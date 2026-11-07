package com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.beastmode_5.App.Presentation.Navigation.BeastMode5Routes
import com.example.beastmode_5.App.Presentation.Navigation.BottomNavigationBar
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeDetailsScreenViewModel
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.BackgroundColor
import com.example.beastmode_5.ui.theme.HomeBackgroundColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText
import com.example.beastmode_5.ui.theme.WarningColor

@Composable
fun PotholeDetailsScreen(viewmodel: PotholeDetailsScreenViewModel,navcontroller: NavController) {

    Scaffold(
        bottomBar = {
            BottomNavigationBar("Reports",navcontroller)
        }
    ) {innerpading ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(HomeBackgroundColor)
                .padding(innerpading)
        )
        {
            when {
                viewmodel.isloading -> LoadingScreen()
                viewmodel.error_msg != null ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = viewmodel.error_msg!!,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = WarningColor
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    viewmodel.getpotholedetails()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 2.dp,
                                    pressedElevation = 4.dp
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WarningColor,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "Retry",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                viewmodel.potholedetails.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                                    contentDescription = "Pothole Details",
                                    tint = AccentBlue,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pothole Details",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryText
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    navcontroller.navigate(BeastMode5Routes.CloudDetailsScreen){
                                        popUpTo (BeastMode5Routes.DetailsScreen){
                                            inclusive=true
                                            saveState=true
                                        }
                                    }
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                   Icon(
                                       imageVector = Icons.Outlined.DirectionsCar,
                                       contentDescription = null,
                                       modifier = Modifier.size(48.dp),
                                       tint = SecondaryText
                                   )
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "No Pothole Details Found",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SecondaryText
                                )
                            }
                        }
                    }
                }
                else->{
                    Box(modifier = Modifier.fillMaxSize()
                        .padding(16.dp)) {
                        FilledPotholeDetails(viewmodel.potholedetails,viewmodel.allareas,viewmodel.selectedarea,
                            ChangeArea = {areaname->
                                viewmodel.ChangeAreaName(areaname)
                            },
                            deleteclick = {id,areaname->
                                viewmodel.updateid(id)
                                viewmodel.updatearea(areaname)
                                viewmodel.Changeshowdialog(true)
                            },
                            CoordinatesClick = {location->
                                navcontroller.navigate(BeastMode5Routes.GoogleMapsScreen(location.latitude,location.longitude)){
                                    popUpTo(BeastMode5Routes.DetailsScreen){
                                        inclusive=true
                                        saveState=true
                                    }
                                }
                                //navigate to Google maps
                            },
                            onClickSync = {
                                viewmodel.SyncData()
                            },viewmodel.isSyncButtonEnabled,
                            onClickSwap = {
                                navcontroller.navigate(BeastMode5Routes.CloudDetailsScreen){
                                    popUpTo (BeastMode5Routes.DetailsScreen){
                                        inclusive=true
                                        saveState=true
                                    }
                                }
                            })

                        if(viewmodel.showdialog){
                            AlertDialogScreen(
                                viewmodel.areanamefordialog,
                                ondismiss = {
                                    viewmodel.Changeshowdialog(false)
                                    viewmodel.updateid(null)
                                    viewmodel.updatearea(null)
                                },
                                ClickedOk = {
                                    viewmodel.Changeshowdialog(false)
                                    viewmodel.idtodelete?.let {id->
                                        viewmodel.deletePothole(id)
                                    }
                                    viewmodel.updateid(null)
                                    viewmodel.updatearea(null)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}