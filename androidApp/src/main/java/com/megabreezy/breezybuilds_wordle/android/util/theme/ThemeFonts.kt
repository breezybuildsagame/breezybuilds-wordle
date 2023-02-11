package com.megabreezy.breezybuilds_wordle.android.util.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.megabreezy.breezybuilds_wordle.android.R

object ThemeFonts
{
    val roboto = FontFamily(
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_black, FontWeight.Black),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_italic, FontWeight.Normal),
        Font(R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_thin, FontWeight.Thin),
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_medium_italic, FontWeight.Medium),
        Font(R.font.roboto_black_italic, FontWeight.Black),
        Font(R.font.roboto_bold_italic, FontWeight.Bold),
        Font(R.font.roboto_thin_italic, FontWeight.Thin),
        Font(R.font.roboto_light_italic, FontWeight.Light)
    )

    val typography = Typography(
        displayLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.SemiBold
        ),
        bodySmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal
        ),
        bodyMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium
        ),
        bodyLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.SemiBold
        ),
        labelSmall = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Normal
        ),
        labelMedium = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.Medium
        ),
        labelLarge = TextStyle(
            fontFamily = roboto,
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }