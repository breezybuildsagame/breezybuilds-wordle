package com.megabreezy.breezybuilds_wordle.core.ui.button.mock

import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable

class ButtonCommonMock(
    var onClick: suspend () -> Unit = { }
): ButtonRepresentable
{
    override suspend fun click() { onClick() }
}