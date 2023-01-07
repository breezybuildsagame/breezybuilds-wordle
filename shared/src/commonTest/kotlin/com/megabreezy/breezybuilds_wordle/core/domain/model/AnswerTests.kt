package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnswerTests
{
    private val exampleWord = Word(word = "TESTER")

    @Test
    fun `when initialized with word - word method returns expected value`()
    {
        // given
        val expectedWord = exampleWord
        
        // when
        val sut = Answer(word = expectedWord)

        // then
        assertEquals(expectedWord, sut.word())
    }
    
    @Test
    fun `when initialized with isCurrent set true - isCurrent method returns true`()
    {
        // when
        val sut = Answer(word = exampleWord, isCurrent = true)

        // then
        assertTrue(sut.isCurrent())
    }
    
    @Test
    fun `when initialized with isCurrent set false - isCurrent method returns false`()
    {
        // when
        val sut = Answer(word = exampleWord, isCurrent = false)
        
        // then
        assertFalse(sut.isCurrent())
    }
    
    @Test
    fun `when setIsCurrent invoked on instance with true parameter - isCurrent method returns true`()
    {
        // given
        val sut = Answer(word = exampleWord, isCurrent = false)
        
        // when
        sut.setIsCurrent(newIsCurrent = true)

        // then
        assertTrue(sut.isCurrent())
    }
    
    @Test
    fun `when setIsCurrent invoked on instance with false parameter - isCurrent method returns false`()
    {
        // given
        val sut = Answer(word = exampleWord, isCurrent = true)

        // when
        sut.setIsCurrent(newIsCurrent = false)

        // then
        assertFalse(sut.isCurrent())
    }
}