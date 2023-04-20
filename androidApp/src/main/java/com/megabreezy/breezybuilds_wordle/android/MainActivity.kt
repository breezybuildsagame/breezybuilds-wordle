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
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.megabreezy.breezybuilds_wordle.android.core.navigation.Navigation
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_modal.rememberAppModalViewHandler
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalStageDimensions
import com.megabreezy.breezybuilds_wordle.android.core.util.rememberGlobalSceneDimensions
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataSource
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.core.util.initKoin
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext.get

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit)
{
    val lightColors = lightColorScheme(
        background = Color(0xFFB4B4B4),
        onBackground = Color(0xFFFFFFFF),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        error = Color(0xFF808080),
        primary = Color(0xFF202020),
        secondary = Color(0xFF83C78A),
        tertiary = Color(0xFFD4BE49),
        surface = Color(0xFF424242)
    )

    val darkColors = darkColorScheme(
        background = Color(0xFFB4B4B4),
        onBackground = Color(0xFFFFFFFF),
        onPrimary = Color(0xFFFFFFFF),
        onSecondary = Color(0xFFFFFFFF),
        onSurface = Color(0xFFFFFFFF),
        error = Color(0xFF808080),
        primary = Color(0xFF202020),
        secondary = Color(0xFF83C78A),
        tertiary = Color(0xFFD4BE49),
        surface = Color(0xFF424242)
    )

    val typography = Typography(
        bodyMedium = TextStyle(
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
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
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
            val wordDataSource: WordLocalDataManageable by inject()
            val realWordDataSource = wordDataSource as? WordLocalDataSource
            val appModalViewHandler = rememberAppModalViewHandler()

            realWordDataSource?.let()
            {
                realWordDataSource.fileReader.context = LocalContext.current
            }

            val globalSceneDimensions = rememberGlobalSceneDimensions()

            LaunchedEffect(Unit) { appModalViewHandler.setUp() }

            MyApplicationTheme()
            {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    globalSceneDimensions.Component()

                    CompositionLocalProvider(LocalStageDimensions provides globalSceneDimensions.stageFrame)
                    {
                        CompositionLocalProvider(LocalSceneDimensions provides globalSceneDimensions.sceneFrame)
                        {
                            Navigation()

                            if (appModalViewHandler.appModalIsShowing)
                            {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.8f))
                                )
                                {
                                    appModalViewHandler.ModalContent()
                                }
                            }
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
