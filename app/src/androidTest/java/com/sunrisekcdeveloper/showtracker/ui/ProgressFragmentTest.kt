package com.sunrisekcdeveloper.showtracker.ui

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.sunrisekcdeveloper.showtracker.R
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class ProgressFragmentTest {

    private lateinit var scenario: FragmentScenario<ProgressFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer<ProgressFragment>(null, R.style.Theme_ShowTracker)
    }

    @Test fun progressFragment_displayed_in_ui() = runBlockingTest {
        onView(withId(R.id.tv_progress)).check(matches(withText("PROGRESS")))
        onView(withId(R.id.sv_search)).check(matches(isDisplayed()))
        onView(withId(R.id.sv_search)).check(matches(withHint("Search Here")))
        scenario.recreate()
    }

}

/**
 * allows typing into SearchViews
 * TODO extract to someplace else
 */
fun typeSearchViewText(text: String): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Change view text"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as SearchView).setQuery(text, false)
        }
    }
}