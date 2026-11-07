package com.example.beastmode_5.App.Presentation.Screens.PotholeDetailsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.CardColor
import com.example.beastmode_5.ui.theme.DarkBackground
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryText

@Composable
fun FilterChipScreen(
    selectedarea:String,
    allareas:List<String>,
    onFilterClick:(String)-> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(allareas){area->
            FilterChip(
                selected = area==selectedarea,
                onClick = {onFilterClick(area)},
                label = {
                    Text(
                        text = area,
                        fontSize = 14.sp,
                        fontWeight = if (area == selectedarea) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        }
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = PrimaryText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = area == selectedarea,
                    borderColor = Color(0xFFD5DDE8),
                    selectedBorderColor = AccentBlue
                )
            )
        }
    }
}