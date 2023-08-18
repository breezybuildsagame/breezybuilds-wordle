package com.megabreezy.breezybuilds_wordle.android.core.ui.app_sheet

import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.megabreezy.breezybuilds_wordle.android.help.presentation.HelpSheetComposable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetHelper
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetViewHandleable
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet
import kotlinx.coroutines.launch

class AppSheetViewHandler(
    private val appSheet: AppSheetRepresentable = AppSheetHelper().appSheet()
): AppSheetViewHandleable
{
    var appSheetIsShowing by mutableStateOf(false)

    fun setUp() { appSheet.setHandler(newHandler = this) }

    override fun onSheetShouldShow(animationDuration: Long) { appSheetIsShowing = true }

    override fun onSheetShouldHide(animationDuration: Long) { appSheetIsShowing = false }

    @Composable
    fun SheetContent()
    {
        val content = appSheet.content()
        val contentScope = rememberCoroutineScope()

        if (content is HelpSheet)
        {
            HelpSheetComposable.Component(
                options = HelpSheetComposable.ComponentOptions(
                    title = content.title(),
                    instructions = content.instructions().map()
                    {
                        {
                            HelpSheetComposable.Instruction.Component(
                                options = HelpSheetComposable.Instruction.ComponentOptions(
                                    instruction = it.instruction()
                                )
                            )
                        }
                    },
                    examples = content.examples().map()
                    { example ->
                        {
                            HelpSheetComposable.Example.Component(
                                options = HelpSheetComposable.Example.ComponentOptions(
                                    tiles = example.tiles().map
                                    { tile ->
                                        {
                                            HelpSheetComposable.Tile.Component(
                                                options = HelpSheetComposable.Tile.ComponentOptions(
                                                    state = tile.state(),
                                                    letter = "$tile"
                                                )
                                            )
                                        }
                                    },
                                    description = example.description()
                                )
                            )
                        }
                    },
                    footer = content.footer(),
                    closeButton = {
                        HelpSheetComposable.CloseButton.Component(
                            options = HelpSheetComposable.CloseButton.ComponentOptions {
                                contentScope.launch { content.closeButton().click() }
                            }
                        )
                    }
                )
            )
        }
    }
}

@Composable
fun rememberAppSheetViewHandler(
    appSheet: AppSheetRepresentable = AppSheetHelper().appSheet()
) = remember {
    AppSheetViewHandler(appSheet = appSheet)
}