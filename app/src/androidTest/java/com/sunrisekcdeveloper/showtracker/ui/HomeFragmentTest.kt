package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
        val scenario = launchFragmentInContainer<HomeFragment>(null, R.style.Theme_ShowTracker)
        onView(withId(R.id.tv_home)).check(matches(withText("HOME")))
        scenario.recreate()
    }
}