package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataSource
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataSource
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.mock.GuessLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataSource
import com.megabreezy.breezybuilds_wordle.core.data.source.word.mock.WordLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigator
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModal
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheet
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetRepresentable
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinModule(val scenarios: List<Scenario> = listOf())
{
    fun mockModule(): Module = module()
    {
        single<AppModalRepresentable> { AppModal() }

        single<AppNavigationHandleable> { AppNavigator() }

        single<AppSheetRepresentable> { AppSheet() }

        AnswerLocalDataSourceMock.injectDefinition(module = this, scenarios = scenarios)

        GuessLocalDataSourceMock.injectDefinition(module = this, scenarios = scenarios)

        WordLocalDataSourceMock.injectDefinition(module = this, scenarios = scenarios)
    }

    fun module(): Module = module()
    {
        single<AppModalRepresentable> { AppModal() }

        single<AppNavigationHandleable> { AppNavigator() }

        single<AppSheetRepresentable> { AppSheet() }

        single<AnswerLocalDataManageable> { AnswerLocalDataSource() }

        single<GuessLocalDataManageable> { GuessLocalDataSource() }

        single<WordLocalDataManageable> { WordLocalDataSource() }
    }
}