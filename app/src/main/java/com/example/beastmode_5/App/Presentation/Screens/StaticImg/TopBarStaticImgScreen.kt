package com.example.beastmode_5.App.Presentation.Screens.StaticImg

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.CardColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.StaticImgSurfaceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarStaticImgScreen(onClicked:()->Unit) {
    TopAppBar(title = {
        Text(
            text = "Image Detection",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryText
        )
    },
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(
                onClick = {
                    onClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AccentBlue
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = StaticImgSurfaceColor,
            titleContentColor = PrimaryText,
            navigationIconContentColor = AccentBlue
        )
        )
}
