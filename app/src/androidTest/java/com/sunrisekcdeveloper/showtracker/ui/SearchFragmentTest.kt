package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sunrisekcdeveloper.showtracker.R
import com.sunrisekcdeveloper.showtracker.ui.screens.SearchFragment
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class SearchFragmentTest {
    @Test fun searchFragment_displayed_in_ui() = runBlockingTest {
        val scenario = launchFragmentInContainer<SearchFragment>(null, R.style.Theme_ShowTracker)
        onView(withId(R.id.tv_search)).check(matches(withText("SEARCH")))
        scenario.recreate()
    }
}