package com.sunrisekcdeveloper.showtracker.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.*
import com.sunrisekcdeveloper.showtracker.R
import okhttp3.internal.notify
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.hamcrest.core.AllOf.*
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test fun bottom_navigation_view_and_elements_is_displayed() {
        // visibility
        onView(withId(R.id.bottom_navigation_main)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_fragment_dest)).check(matches(isDisplayed()))
        onView(withId(R.id.home_fragment_dest)).check(matches(isDisplayed()))
        onView(withId(R.id.search_fragment_dest)).check(matches(isDisplayed()))

        // status
        onView(withId(R.id.progress_fragment_dest)).check(matches(isSelected()))
        onView(withId(R.id.home_fragment_dest)).check(matches(not(isSelected())))
        onView(withId(R.id.search_fragment_dest)).check(matches(not(isSelected())))
    }

    @Test fun navigate_to_each_bottom_nav_option() {
        // home
        onView(withId(R.id.home_fragment_dest)).perform(click())
        onView(withId(R.id.home_fragment_dest)).check(matches(isSelected()))
        // progress
        onView(withId(R.id.progress_fragment_dest)).perform(click())
        onView(withId(R.id.progress_fragment_dest)).check(matches(isSelected()))
        // search
        onView(withId(R.id.search_fragment_dest)).perform(click())
        onView(withId(R.id.search_fragment_dest)).check(matches(isSelected()))
    }
}