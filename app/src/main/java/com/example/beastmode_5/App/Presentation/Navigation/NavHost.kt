package com.example.beastmode_5.App.Presentation.Navigation

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.beastmode_5.App.Presentation.Screens.CameraPreview.CameraPreview
import com.example.beastmode_5.App.Presentation.Screens.GoogleMapsScreen.GoogleMapsScreen
import com.example.beastmode_5.App.Presentation.Screens.HomeScreen.HomeScreen
import com.example.beastmode_5.App.Presentation.Screens.LoginScreen.LoginScreen
import com.example.beastmode_5.App.Presentation.Screens.PotholeCloudDetailsScreen.PotholeCloudDetails
import com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen.PotholeDetailsScreen
import com.example.beastmode_5.App.Presentation.Screens.SignUpScreen.SignUpScreen
import com.example.beastmode_5.App.Presentation.Screens.StaticImg.StaticImgTest
import com.example.beastmode_5.App.Presentation.Screens.UserDetailsScreen.UserDetailsScreen
import com.example.beastmode_5.App.Presentation.Viewmodels.AuthenticationViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.GoogleMapsScreenViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.HomeScreenViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeCloudDetailsViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeDetailsScreenViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.UserDetailsScreenViewModel
import com.google.android.gms.maps.model.LatLng

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun NavHost(application: Application) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = BeastMode5Routes.LoginScreen
    ){
        composable<BeastMode5Routes.LoginScreen>{
            val authviewmodel: AuthenticationViewModel=viewModel {
                AuthenticationViewModel(application)
            }
            LoginScreen(navController = navController, viewModel = authviewmodel)
        }

        composable<BeastMode5Routes.SignupScreen>{
            val authviewmodel: AuthenticationViewModel=viewModel {
                AuthenticationViewModel(application)
            }
            SignUpScreen(navController = navController, viewModel = authviewmodel)
        }

        composable<BeastMode5Routes.HomeScreen>{

            val homeviewmodel: HomeScreenViewModel= viewModel{
                HomeScreenViewModel(application)
            }

            HomeScreen(homeviewmodel,navController = navController)
        }

        composable<BeastMode5Routes.LiveFeedScreen>{

            val potholeviewmodel: PotholeViewModel=viewModel{
                PotholeViewModel(application)
            }

            CameraPreview(potholeviewmodel = potholeviewmodel, navController = navController)
        }

        composable <BeastMode5Routes.StaticFeedScreen>{
            StaticImgTest(navcontroller = navController)
        }

        composable<BeastMode5Routes.DetailsScreen>{

            val potholedetailsviewmodel: PotholeDetailsScreenViewModel =viewModel{
                PotholeDetailsScreenViewModel(application)
            }

            PotholeDetailsScreen(potholedetailsviewmodel, navcontroller = navController)
        }

        composable<BeastMode5Routes.GoogleMapsScreen>{backstackentry->

            val Googlemapsviewmodel: GoogleMapsScreenViewModel=viewModel {
                GoogleMapsScreenViewModel(application)
            }

            val data=backstackentry.toRoute<BeastMode5Routes.GoogleMapsScreen>()
            val lat=data.lat
            val long=data.long
            val latlong= LatLng(lat,long)
            GoogleMapsScreen(latlong,Googlemapsviewmodel, navcontroller = navController)
        }

        composable<BeastMode5Routes.GoogleMapsScreenNoData>{

            val Googlemapsviewmodel: GoogleMapsScreenViewModel=viewModel {
                GoogleMapsScreenViewModel(application)
            }

            GoogleMapsScreen(null,Googlemapsviewmodel, navcontroller = navController)
        }

        composable<BeastMode5Routes.CloudDetailsScreen>{

            val ClodScreenViewModel:PotholeCloudDetailsViewModel=viewModel{
                PotholeCloudDetailsViewModel(application)
            }

            PotholeCloudDetails(ClodScreenViewModel,navcontroller = navController)
        }

        composable<BeastMode5Routes.UserDetailsScreen> {

            val userdetailsviewmodel: UserDetailsScreenViewModel =viewModel{
                UserDetailsScreenViewModel(application)
            }

            UserDetailsScreen(userdetailsviewmodel,navcontroller = navController)
        }
    }
}