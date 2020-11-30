package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
class ProgressFragmentTest {
    @Test fun progressFragment_displayed_in_ui() = runBlockingTest {
        val scenario = launchFragmentInContainer<ProgressFragment>(null, R.style.Theme_ShowTracker)
        onView(withId(R.id.tv_progress)).check(matches(withText("PROGRESS")))
        scenario.recreate()
    }
}