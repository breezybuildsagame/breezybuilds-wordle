package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class StatTests
{
    @Test
    fun `When entity initialized with headline - headline method returns expected string value`()
    {
        // given
        val expectedHeadline = "10"

        // when
        val sut = Stat(headline = expectedHeadline)

        // then
        assertEquals(expectedHeadline, sut.headline())
    }

    @Test
    fun `When entity initialized with description - description method returns expected string value`()
    {
        // given
        val expectedDescription = "Current win streak"

        // when
        val sut = Stat(description = expectedDescription)

        // then
        assertEquals(expectedDescription, sut.description())
    }
}