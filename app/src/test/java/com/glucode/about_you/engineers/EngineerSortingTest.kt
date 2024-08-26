package com.glucode.about_you.engineers

import android.net.Uri
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.engineers.models.QuickStats
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class EngineerSortingTest {
    @Test
    fun testSortEngineersByQuickStats() {

        // Create a mock Uri
        val mockUri = mock(Uri::class.java)!!

        // Create mock engineers with only QuickStats and name
        val engineer1 = Engineer("Engineer 1", "", mockUri, QuickStats(10, 20, 30), listOf())
        val engineer2 = Engineer("Engineer 2", "", mockUri, QuickStats(30, 20, 10), listOf())
        val engineer3 = Engineer("Engineer 3", "", mockUri, QuickStats(20, 30, 20), listOf())

        val engineers = listOf(engineer1, engineer2, engineer3)

        // Sort the engineers by quick stats
        val sortedEngineers = engineers.sortedBy { it.quickStats.years }

        // Assert that the sorted list is in the correct order
        assertEquals(engineer1, sortedEngineers[0])
        assertEquals(engineer3, sortedEngineers[1])
        assertEquals(engineer2, sortedEngineers[2])
    }

}