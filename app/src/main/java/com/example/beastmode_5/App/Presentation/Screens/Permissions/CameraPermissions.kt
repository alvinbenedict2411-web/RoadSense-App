package com.example.beastmode_5.App.Presentation.Screens.Permissions

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beastmode_5.App.Presentation.Navigation.NavHost
import com.example.beastmode_5.App.Presentation.Screens.CameraPreview.CameraPreview
import com.example.beastmode_5.App.Presentation.Viewmodels.CameraPermissionsViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeViewModel
import com.example.beastmode_5.R
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.CardColor
import com.example.beastmode_5.ui.theme.DarkBackground
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun CameraPermissions(application: Application,viewmodel: CameraPermissionsViewModel= viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        val viewmodelpothole: PotholeViewModel = viewModel() {
            PotholeViewModel(application)
        }
        val context = LocalContext.current

        val permissionslauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            viewmodel.updatePermissionsGranted(PermissiosnGrantedCheck(viewmodel, context))
            viewmodel.updateShowAlertDialog(!PermissiosnGrantedCheck(viewmodel, context))
        }

        LaunchedEffect(viewmodel.permissionsGranted){
            if (!viewmodel.permissionsGranted){
                permissionslauncher.launch(
                    viewmodel.permissions
                )
            }
        }

        if (viewmodel.permissionsGranted){
            NavHost(application)
        }
        else{
            Image(
                painter = painterResource(R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Image(painter = painterResource(R.drawable.icon2),
                contentDescription = "App Icon",
                modifier = Modifier.size(160.dp)
                    .align(Alignment.Center))
        }

        if (viewmodel.showalertdialog) {
            AlertDialog(
                onDismissRequest = {
                    viewmodel.updateShowAlertDialog(false)
                    permissionslauncher.launch(
                        viewmodel.permissions
                    )
                },
                shape = RoundedCornerShape(16.dp),
                title = {
                    Text(
                        text = "Permission Required",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HomePrimaryText
                    )
                },
                text = {
                    Text(
                        text = "Camera and location permissions are required to use pothole detection.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = HomeSecondaryText
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewmodel.updateShowAlertDialog(false)
                            permissionslauncher.launch(
                                viewmodel.permissions

                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentBlue,
                            contentColor = DarkBackground
                        )
                    ) {
                        Text(
                            text = "Try Again",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                containerColor = CardColor,
                titleContentColor = PrimaryText,
                textContentColor = SecondaryText,
                dismissButton = {
                    Button(
                        onClick = {
                            viewmodel.updateShowAlertDialog(false)
                            permissionslauncher.launch(
                                viewmodel.permissions

                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardColor,
                            contentColor = HomePrimaryText
                        )
                    ) {
                        Text(text = "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = SecondaryText)
                    }
                }
            )
        }
    }
}

fun PermissiosnGrantedCheck(viewmodel: CameraPermissionsViewModel,context: Context): Boolean{
    return viewmodel.permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        )== PackageManager.PERMISSION_GRANTED
    }
}