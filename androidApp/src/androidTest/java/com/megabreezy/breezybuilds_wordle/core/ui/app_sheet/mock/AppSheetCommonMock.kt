package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetViewHandleable
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet

class AppSheetCommonMock: AppSheetRepresentable
{
    var contentToReturn: AppSheetContentRepresentable? = HelpSheet(
        title = "Help",
        instructions = listOf(
            HelpSheet.Instruction(instruction = "Mock Instruction")
        ),
        examples = listOf(
            HelpSheet.Example(
                description = "Mock description..",
                tiles = listOf(
                    HelpSheet.Tile(
                        letter = 'G',
                        state = HelpSheet.Tile.State.HIDDEN
                    )
                )
            )
        ),
        footer = "Mock Footer"
    )
    var handlerToReturn: AppSheetViewHandleable? = null

    override fun content() = contentToReturn

    override fun handler() = handlerToReturn

    override fun setContent(newContent: AppSheetContentRepresentable?)
    {
        contentToReturn = newContent
    }

    override fun setHandler(newHandler: AppSheetViewHandleable?)
    {
        handlerToReturn = newHandler
    }

}