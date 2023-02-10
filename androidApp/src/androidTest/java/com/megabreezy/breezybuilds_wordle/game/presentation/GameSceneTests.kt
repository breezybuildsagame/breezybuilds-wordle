package com.megabreezy.breezybuilds_wordle.game.presentation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.GameScene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneKeyboard
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.util.KoinPlatformManager
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() { KoinPlatformManager.start(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)) }

    @After
    fun tearDown() { KoinPlatformManager.stop() }

    @Test
    fun when_view_appears__handler_setUp_is_invoked()
    {
        // given
        var expectedHeaderBottomPadding: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints(modifier = Modifier.fillMaxSize())
                {
                    expectedHeaderBottomPadding = this.maxHeight * (63 / Scene.idealFrame().height)
                }

                GameScene.Stage()
            }
        }
        val container = composeTestRule.onNodeWithContentDescription(GameScene.TagName.CONTAINER.toString(), useUnmergedTree = true)

        // then
        container.onChildAt(index = 0).assertContentDescriptionEquals(GameSceneHeader.TagName.COMPONENT.toString())
        container.onChildAt(index = 1).assertContentDescriptionEquals("spacer")
        container.onChildAt(index = 1).assertHeightIsEqualTo(expectedHeaderBottomPadding!!)
        container.onChildAt(index = 2).assertContentDescriptionEquals(GameSceneBoard.TagName.BOARD.toString())
        container.onChildAt(index = 3).assertContentDescriptionEquals("spacer")
        container.onChildAt(index = 3).assertHeightIsAtLeast(expectedHeaderBottomPadding!!)
        container.onChildAt(index = 4).assertContentDescriptionEquals(GameSceneKeyboard.TagName.ROWS.toString())
    }

    @Test
    fun when_announcement_appears__expected_announcement_is_displayed()
    {
        // given

        // when

        // then
    }
}