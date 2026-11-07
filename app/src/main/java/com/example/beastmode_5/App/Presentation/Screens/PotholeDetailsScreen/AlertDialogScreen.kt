package com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import com.example.beastmode_5.ui.theme.LargeColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText
import com.example.beastmode_5.ui.theme.StaticImgBackgroundColor

@Composable
fun AlertDialogScreen(
    areaname: String?,
    ondismiss: () -> Unit,
    ClickedOk: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        containerColor = HomeSurfaceColor,
        tonalElevation = 0.dp,
        onDismissRequest = {
            ondismiss()
        },
        confirmButton = {
            Button(onClick = {
                ClickedOk()

            },
                shape= RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LargeColor,
                    contentColor = Color.White)
            ) {
                Text("Delete",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            Button(onClick = {
                ondismiss()
            },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = HomePrimaryText.copy(alpha = 0.4f),
                    contentColor = HomeSurfaceColor
                )) {
                Text("Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        },
        title = {
            Text(
                text = "Delete Pothole Report?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HomePrimaryText,
                lineHeight = 26.sp
            )
        },
        text = {
            Text(
                text = "Pothole detected in ${areaname}.\n\nDo you want to permanently delete this report?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = HomeSecondaryText,
                lineHeight = 21.sp
            )
        }
    )
}