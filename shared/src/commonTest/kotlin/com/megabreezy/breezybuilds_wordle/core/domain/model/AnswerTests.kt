package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlin.test.*

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

    @Test
    fun `when initialized and playerGuessedCorrectly method invoked - null is returned`()
    {
        // given, when
        val sut = Answer(word = exampleWord)

        // then
        assertNull(sut.playerGuessedCorrectly())
    }

    @Test
    fun `when initialized with playerGuessedCorrectly nullable boolean parameter set true - playerGuessedCorrectly method returns true`()
    {
        // given, when
        val sut = Answer(word = exampleWord, playerGuessedCorrectly = true)

        // then
        assertTrue(sut.playerGuessedCorrectly() ?: false)
    }

    @Test
    fun `when initialized with playerGuessedCorrectly nullable boolean parameter set false - playerGuessedCorrectly method returns true`()
    {
        // given, when
        val sut = Answer(word = exampleWord, playerGuessedCorrectly = false)

        // then
        assertFalse(sut.playerGuessedCorrectly() ?: true)
    }

    @Test
    fun `When setPlayerGuessedCorrectly method invoked on instance with true parameter - playerGuessedCorrectly method returns true`()
    {
        // given
        val sut = Answer(word = exampleWord, playerGuessedCorrectly = false)

        // when
        sut.setPlayerGuessedCorrectly(newPlayerGuessedCorrectly = true)

        // then
        assertTrue(sut.playerGuessedCorrectly() ?: false)
    }

    @Test
    fun `When setPlayerGuessedCorrectly method invoked on instance with false parameter - playerGuessedCorrectly method returns false`()
    {
        // given
        val sut = Answer(word = exampleWord, playerGuessedCorrectly = true)

        // when
        sut.setPlayerGuessedCorrectly(newPlayerGuessedCorrectly = false)

        // then
        assertFalse(sut.playerGuessedCorrectly() ?: false)
    }
}