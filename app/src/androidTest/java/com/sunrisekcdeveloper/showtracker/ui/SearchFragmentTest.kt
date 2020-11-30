package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sunrisekcdeveloper.showtracker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class SearchFragmentTest {
    @Test fun searchFragment_displayed_in_ui() = runBlockingTest {
        launchFragmentInContainer<SearchFragment>(null, R.style.Theme_ShowTracker)
        delay(4000)
    }
}