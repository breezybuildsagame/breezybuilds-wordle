package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import kotlin.test.Test
import kotlin.test.assertEquals

class CachedAnswerTests
{
    @Test
    fun `When initialized with answer - word property returns expected word string value`()
    {
        // given
        val expectedAnswer = Answer(word = Word(word = "SPIKES"))

        // when
        val sut = CachedAnswer(answer = expectedAnswer)

        // then
        assertEquals(expectedAnswer.word().toString(), sut.word)
        assertEquals(expectedAnswer.isCurrent(), sut.isCurrent)
    }

    @Test
    fun `When initialized with non-current answer - word property returns expected word string value`()
    {
        // given
        val expectedAnswer = Answer(word = Word(word = "SNAKES"), isCurrent = true)

        // when
        val sut = CachedAnswer(answer = expectedAnswer)

        // then
        assertEquals(expectedAnswer.word().toString(), sut.word)
        assertEquals(expectedAnswer.isCurrent(), sut.isCurrent)
    }
}