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

package com.sunrisekcdeveloper.showtracker.features.discover.client

import com.sunrisekcdeveloper.showtracker.CoroutineTestRule
import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseImages
import com.sunrisekcdeveloper.showtracker.models.network.base.ResponseMovie
import com.sunrisekcdeveloper.showtracker.models.network.envelopes.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.greaterThan
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.hamcrest.Matchers.hasSize
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.collection.IsMapContaining

@ExperimentalCoroutinesApi
class DiscoveryClientTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val apiFake = DiscoveryServiceContract.Fake()
    lateinit var remoteSource: DiscoveryRemoteDataSourceContract

    @Before
    fun setup() {
        remoteSource = DiscoveryClient(
            apiFake,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun fetchFeaturedMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchFeaturedMovies()

        assertThat(actual.size, `is`(greaterThan(0)))
    }

    @Test
    fun fetchFeaturedMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchFeaturedMovies()

        assertThat(actual.size, `is`(0))
    }

    @Test
    fun fetchBoxOfficeMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchBox()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeRevenue::class.java))
    }

    @Test
    fun fetchBoxOfficeMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchBox()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchTrendingMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchTrend()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeWatchers::class.java))
    }

    @Test
    fun fetchTrendingMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchTrend()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchPopularMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchPop()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(ResponseMovie::class.java))
    }

    @Test
    fun fetchPopularMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchPop()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchMostPlayedMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchMostPlayed()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeViewStats::class.java))
    }

    @Test
    fun fetchMostPlayedMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchMostPlayed()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchMostWatchedMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchMostWatched()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeViewStats::class.java))
    }

    @Test
    fun fetchMostWatchedMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchMostWatched()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchMostAnticipatedMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchAnticipated()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeListCount::class.java))
    }

    @Test
    fun fetchMostAnticipatedMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchAnticipated()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchRecommendedMovies_Happy() = runBlockingTest {
        val actual = remoteSource.fetchRecommended()

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, not(IsEmptyCollection.empty()))
        assertThat(data, hasSize(10))
        assertThat(data[0], instanceOf(EnvelopeUserCount::class.java))
    }

    @Test
    fun fetchRecommendedMovies_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.fetchRecommended()
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }

    @Test
    fun fetchPosterImages_Happy() = runBlockingTest {
        val actual = remoteSource.poster("id")

        val data = (actual as Resource.Success).data

        assertThat(actual, instanceOf(Resource.Success::class.java))
        assertThat(data, instanceOf(ResponseImages::class.java))
    }

    @Test
    fun fetchPosterImages_Fail() = runBlockingTest {
        apiFake.happyPath = false
        val actual = remoteSource.poster("id")
        assertThat(actual, instanceOf(Resource.Error::class.java))
    }
}