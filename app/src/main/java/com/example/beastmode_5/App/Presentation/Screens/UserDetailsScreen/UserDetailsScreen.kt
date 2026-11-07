package com.example.beastmode_5.App.Presentation.Screens.UserDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.beastmode_5.App.Presentation.Navigation.BeastMode5Routes
import com.example.beastmode_5.App.Presentation.Navigation.BottomNavigationBar
import com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen.LoadingScreen
import com.example.beastmode_5.App.Presentation.Viewmodels.UserDetailsScreenViewModel
import com.example.beastmode_5.ui.theme.HomeBackgroundColor
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.WarningColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(viewmodel: UserDetailsScreenViewModel,navcontroller: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(selectedScreen = "Profile", navController = navcontroller)
        }) { innerpadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(HomeBackgroundColor)
                .padding(innerpadding)
        ) {
            when {
                viewmodel.islaoding -> {
                    LoadingScreen()
                }

                viewmodel.errormsg != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = viewmodel.errormsg!!,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarningColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewmodel.getcloudlivedatastarted()
                                viewmodel.getlivedataroom()
                                viewmodel.getuserdata()
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

                else -> {
                    UserDetailFilled(
                        viewmodel.username,
                        viewmodel.usernameforchange,
                        viewmodel.useremail,
                        viewmodel.userIcon,
                        viewmodel.allpotholesfromroomCount,
                        viewmodel.allareasfromroomCount,
                        viewmodel.allpotholesfromcloudCount,
                        viewmodel.allareasfromcloud,
                        viewmodel.totalreportsinroom,
                        viewmodel.WaitingtoSync,
                        viewmodel.CloudStatus,
                        onEditClick = {
                            viewmodel.UpdateUserNameforchangeonEditClickButton()
                            viewmodel.updateshowbottomsheetupdatesettrue()
                        },
                        onLogoutClick = {
                            viewmodel.updateshowalertdialogupdatesettrue()
                        },
                        onClickRefresh = {
                            viewmodel.reloaduserdata()
                        })
                }
            }
        }
    }
    if (viewmodel.showalertdialogforusersignout) {
        AlertDialog(
            onDismissRequest = {
                viewmodel.updateshowalertdialogupdatesetfalse()
            },

            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = Color(0xFFFFE8E8),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD93636),
                        modifier = Modifier.size(30.dp)
                    )
                }
            },

            title = {
                Text(
                    text = "Logout?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomePrimaryText
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to log out of RoadSense? " +
                            "You will need to sign in again to access your account.",
                    fontSize = 14.sp,
                    color = HomeSecondaryText,
                    fontWeight = FontWeight.Normal
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        viewmodel.SignOutUser()
                        viewmodel.updateshowalertdialogupdatesetfalse()
                        // Navigation to the Login screen will be handled here.
                        navcontroller.navigate(BeastMode5Routes.LoginScreen) {
                            popUpTo(navcontroller.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD93636),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.height(0.dp).width(8.dp))
                    Text(
                        text = "Logout",
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        viewmodel.updateshowalertdialogupdatesetfalse()
                    }
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Medium,
                        color = HomePrimaryText
                    )
                }
            },

            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (viewmodel.showbottomsheetforusernamechange) {
        ModalBottomSheet(
            onDismissRequest = {
                viewmodel.updateshowbottomsheetupdatesetfalse()
                viewmodel.UpdateUserNameforchangeonEditClickButton()
            },
            shape = RoundedCornerShape(
                topStart = 28.dp,
                topEnd = 28.dp
            ),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Edit Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF172B4D)
                )

                Text(
                    text = "Update your display name",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EEF9)),
                    contentAlignment = Alignment.Center
                ) {
                    if (viewmodel.userIcon != null) {
                        AsyncImage(
                            model = viewmodel.userIcon,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default profile picture",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF345995)
                        )
                    }
                }

                OutlinedTextField(
                    value = viewmodel.usernameforchange,
                    onValueChange = {
                        viewmodel.UpdateUserNameForChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Display Name",
                            color = HomePrimaryText)
                    },
                    placeholder = {
                        Text("Enter your name",
                            color = HomePrimaryText)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HomePrimaryText,
                        unfocusedBorderColor = HomePrimaryText,
                        focusedTextColor = HomePrimaryText,
                        unfocusedTextColor = HomePrimaryText
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF3F5F9),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Email Address",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewmodel.useremail,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF172B4D)
                    )
                }
                    // Save Changes button
                    Button(
                        onClick = {
                            viewmodel.UpdateUsername(
                                viewmodel.usernameforchange
                            )
                            viewmodel.updateshowbottomsheetupdatesetfalse()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = viewmodel.usernameforchange.isNotBlank(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3568C0),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Changes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
            }
        }
    }
}
