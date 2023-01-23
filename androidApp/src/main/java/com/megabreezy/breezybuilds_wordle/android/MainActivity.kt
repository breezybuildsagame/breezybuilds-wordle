package com.megabreezy.breezybuilds_wordle.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.megabreezy.breezybuilds_wordle.Greeting
import com.megabreezy.breezybuilds_wordle.android.core.navigation.Navigation
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalStageDimensions
import com.megabreezy.breezybuilds_wordle.android.core.util.rememberGlobalSceneDimensions
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.core.util.initKoin
import org.koin.core.context.GlobalContext.get

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5)
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5)
        )
    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try { get() }
        catch(e: Throwable) {
            initKoin(
                scenarios = listOf(
                    Scenario.WORD_FOUND,
                    Scenario.ANSWER_SAVED
                )
            )
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent()
        {
            val globalSceneDimensions = rememberGlobalSceneDimensions()

            MyApplicationTheme()
            {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                )
                {
                    globalSceneDimensions.Component()

                    CompositionLocalProvider(LocalStageDimensions provides globalSceneDimensions.stageFrame)
                    {
                        CompositionLocalProvider(LocalSceneDimensions provides globalSceneDimensions.sceneFrame)
                        {
                            Navigation()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Hello, Android!")
    }
}
