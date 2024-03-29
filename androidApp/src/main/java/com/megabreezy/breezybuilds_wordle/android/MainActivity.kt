package com.megabreezy.breezybuilds_wordle.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.megabreezy.breezybuilds_wordle.android.core.navigation.Navigation
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_modal.rememberAppModalViewHandler
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_sheet.rememberAppSheetViewHandler
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
        catch(e: Throwable) { initKoin(scenarios = listOf()) }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent()
        {
            val wordDataSource: WordLocalDataManageable by inject()
            val realWordDataSource = wordDataSource as? WordLocalDataSource
            val density = LocalDensity.current
            val appModalViewHandler = rememberAppModalViewHandler(scope = rememberCoroutineScope())
            val appSheetViewHandler = rememberAppSheetViewHandler()

            realWordDataSource?.let()
            {
                realWordDataSource.fileReader.context = LocalContext.current
            }

            val globalSceneDimensions = rememberGlobalSceneDimensions()

            LaunchedEffect(Unit)
            {
                appModalViewHandler.setUp()
                appSheetViewHandler.setUp()
            }

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
                            Box(
                                modifier = Modifier
                                    .blur(radius = LocalSceneDimensions.current.height.times(
                                        (if (appSheetViewHandler.appSheetIsShowing) 6f else 0f) / Scene.idealFrame().height)
                                    )
                            )
                            {
                                Navigation()
                            }

                            if (appModalViewHandler.appModalIsShowing)
                            {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.8f))
                                )
                                {
                                    appModalViewHandler.ModalContent()
                                }
                            }

                            val topPadding = LocalStageDimensions.current.height
                            AnimatedVisibility(
                                visible = appSheetViewHandler.appSheetIsShowing,
                                enter = slideInVertically {
                                    with(density) { topPadding.roundToPx() }
                                },
                                exit = slideOutVertically {
                                    with(density) { topPadding.roundToPx() }
                                }
                            )
                            {
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shadowElevation = LocalSceneDimensions.current.height.times(6 / Scene.idealFrame().height),
                                    shape = RoundedCornerShape(corner = CornerSize(size = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height))),
                                    modifier = Modifier
                                        .width(LocalStageDimensions.current.width)
                                        .padding(top = LocalSceneDimensions.current.height.times(150 / Scene.idealFrame().height))
                                )
                                {
                                    Box(
                                        contentAlignment = Alignment.BottomCenter,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    {
                                        appSheetViewHandler.SheetContent()
                                    }
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
