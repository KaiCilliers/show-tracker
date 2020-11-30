package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.MediumTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sunrisekcdeveloper.showtracker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    @Test fun homefragment_displayed_in_ui() = runBlockingTest {
        launchFragmentInContainer<HomeFragment>(null, R.style.Theme_ShowTracker)
        delay(4000)
    }
}