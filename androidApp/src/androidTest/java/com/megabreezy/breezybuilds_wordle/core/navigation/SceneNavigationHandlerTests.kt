package com.megabreezy.breezybuilds_wordle.core.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.navigation.SceneNavigationHandler
import com.megabreezy.breezybuilds_wordle.core.navigation.mock.AppNavigatorCommonMock
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SceneNavigationHandlerTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_setUp_method_invoked_AppNavigationHandleable_setHandler_method_is_invoked_passing_in_self()
    {
        // given
        val mockNavigator = AppNavigatorCommonMock()
        val sut = SceneNavigationHandler(appNavigator = mockNavigator)

        // when
        sut.setUp()

        // then
        Assert.assertNotNull(mockNavigator.currentSceneNavigator)
    }
}