package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.data.source.word.mock.WordLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinModule(val scenarios: List<Scenario> = listOf())
{
    fun mockModule(): Module = module()
    {
        single<AppNavigationHandleable> { AppNavigator() }

        AnswerLocalDataSourceMock.injectDefinition(module = this, scenarios = scenarios)

        WordLocalDataSourceMock.injectDefinition(module = this, scenarios = scenarios)
    }
}