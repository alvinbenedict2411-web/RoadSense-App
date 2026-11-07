package com.example.beastmode_5.App.Presentation.Navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.BackgroundColor
import com.example.beastmode_5.ui.theme.CardColor
import com.example.beastmode_5.ui.theme.SecondaryText

@Composable
fun BottomNavigationBar(selectedScreen: String,navController: NavController) {
    val items=listOf(
        AllScreen(Icons.Default.Home,
            "Home", BeastMode5Routes.HomeScreen),
        AllScreen(
            Icons.AutoMirrored.Filled.ReceiptLong,
            "Reports", BeastMode5Routes.DetailsScreen),
        AllScreen(Icons.Default.Map,
            "Maps", BeastMode5Routes.GoogleMapsScreenNoData),
        AllScreen(Icons.Default.AccountBox,
            "Profile", BeastMode5Routes.UserDetailsScreen)
    )
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = CardColor,
        contentColor = AccentBlue,
        tonalElevation = 3.dp
    ) {
        items.forEach { item->
            NavigationBarItem(selected = selectedScreen==item.name,
                onClick = {navController.navigate(item.route){
                    popUpTo(BeastMode5Routes.HomeScreen){
                        saveState=true
                    }
                    launchSingleTop=true
                    restoreState=true
                }
                },
                icon={
                    Icon(imageVector = item.icon, contentDescription = item.name)
                },
                label = {
                    Text(text = item.name)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentBlue,
                    selectedTextColor = AccentBlue,
                    unselectedIconColor = SecondaryText,
                    unselectedTextColor = SecondaryText,
                    indicatorColor = BackgroundColor
                ))
        }

    }
}
data class AllScreen(
    val icon: ImageVector,
    val name: String,
    val route: BeastMode5Routes
)