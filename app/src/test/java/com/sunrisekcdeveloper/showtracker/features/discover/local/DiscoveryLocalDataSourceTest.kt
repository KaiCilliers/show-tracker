/*
 * Copyright © 2021. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.features.discover.local

import com.sunrisekcdeveloper.showtracker.CoroutineTestRule
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedMovies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.Is.`is`
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock

//TODO fix tests
@ExperimentalCoroutinesApi
class DiscoveryLocalDataSourceTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val daoMock = mock(DiscoveryDao::class.java)
    lateinit var localSource: DiscoveryLocalDataSourceContract

    @Before
    fun setup() {
        localSource = DiscoveryLocalDataSource(daoMock)
    }

    @Test
    fun featuredMovies_Happy() = runBlockingTest {
        val actual = localSource.featuredMovies()
        assertThat(actual.size, `is`(greaterThan(0)))
        assertThat(actual[0], instanceOf(FeaturedMovies::class.java))
    }
}