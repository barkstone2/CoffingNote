package com.note.coffee.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.note.coffee.R

val largeTextStyle = TextStyle(
    fontFamily = Font(R.font.maplestory_light).toFontFamily(),
    fontSize = 20.sp
)
val mediumTextStyle = TextStyle(
    fontFamily = Font(R.font.maplestory_light).toFontFamily(),
    fontSize = 18.sp
)
val smallTextStyle = TextStyle(
    fontFamily = Font(R.font.maplestory_light).toFontFamily(),
    fontSize = 15.sp,
)

val Typography = Typography(
    displayLarge = largeTextStyle,
    displayMedium = mediumTextStyle,
    displaySmall = smallTextStyle,
    headlineLarge = largeTextStyle,
    headlineMedium = mediumTextStyle,
    headlineSmall = smallTextStyle,
    titleLarge = largeTextStyle.copy(fontWeight = FontWeight.Bold),
    titleMedium = mediumTextStyle.copy(fontWeight = FontWeight.Bold),
    titleSmall = smallTextStyle.copy(fontWeight = FontWeight.Bold),
    bodyLarge = largeTextStyle,
    bodyMedium = mediumTextStyle,
    bodySmall = smallTextStyle,
    labelLarge = largeTextStyle,
    labelMedium = mediumTextStyle,
    labelSmall = smallTextStyle,
)