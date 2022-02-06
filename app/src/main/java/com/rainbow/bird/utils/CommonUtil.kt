package com.rainbow.bird.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.bird.R

object CommonUtil {
    fun getRandomDp(fromDp: Dp, toDp: Dp): Dp = (fromDp.value.toInt()..toDp.value.toInt()).random().dp

}
val ScoreFontFamily = FontFamily(
    Font(R.font.riskofrainsquare, FontWeight.Bold)
)