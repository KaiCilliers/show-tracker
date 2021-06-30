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

package com.sunrisekcdeveloper.showtracker.common.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.model.EntityMovie
import com.sunrisekcdeveloper.showtracker.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.isNotNull

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoMovieTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoMovie

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            TrackerDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = db.movieDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_movie() = runBlockingTest {
        dao.insert(TestUtil.createEntityMovie(id = 54, title = "Title"))
        val record = dao.withId("54")

        assertNotNull(record)
        assertEquals(record?.title, "Title")
        assertEquals(record?.id, "54")
    }

    @Test
    fun insert_multiple_movies() = runBlockingTest {
        dao.insert(
            TestUtil.createEntityMovie(id = 22, title = "one"),
            TestUtil.createEntityMovie(id = 23, title = "two")
        )
        val movieOne = dao.withId("22")
        val movieTwo = dao.withId("23")

        assertNotNull(movieOne)
        assertNotNull(movieTwo)
        assertEquals(movieOne?.id, "22")
        assertEquals(movieOne?.title, "one")
        assertEquals(movieTwo?.id, "23")
        assertEquals(movieTwo?.title, "two")
    }

    @Test
    fun replace_movie_on_matching_ids() = runBlockingTest {
        dao.insert(TestUtil.createEntityMovie(id = 1, title = "one"))
        dao.insert(TestUtil.createEntityMovie(id = 1, title = "two"))

        val movie = dao.withId("1")

        assertNotNull(movie)
        assertEquals(movie?.id, "1")
        assertEquals(movie?.title, "two")
    }

    @Test
    fun update_existing_movie_all_fields_except_id() = runBlockingTest {
        dao.insert(TestUtil.createEntityMovie(id = 1, title = "one"))

        dao.withId("1").apply {
            this?.let {
                dao.update(copy(
                    title = "newTitle",
                    overview = "newOverview",
                    posterPath = "newPosterPath",
                    backdropPath = "newBackdropPath",
                    rating = -88f,
                    popularityValue = -88f,
                    certification = "newCertification",
                    releaseDate = "newReleaseDate",
                    runTime = "newRunTime",
                    dateLastUpdated = -88L
                ))
            }
        }

        val movie = dao.withId("1")

        assertNotNull(movie)
        assertEquals(movie?.title, "newTitle")
        assertEquals(movie?.overview, "newOverview")
        assertEquals(movie?.posterPath, "newPosterPath")
        assertEquals(movie?.backdropPath, "newBackdropPath")
        assertEquals(movie?.rating, -88f)
        assertEquals(movie?.popularityValue, -88f)
        assertEquals(movie?.certification, "newCertification")
        assertEquals(movie?.releaseDate, "newReleaseDate")
        assertEquals(movie?.runTime, "newRunTime")
        assertEquals(movie?.dateLastUpdated, -88L)
    }

    @Test
    fun delete_a_movie() = runBlockingTest {
        dao.insert(TestUtil.createEntityMovie(id = 1))
        dao.withId("1")?.let { dao.delete(it) }

        val movie = dao.withId("id")

        assertNull(movie)
    }

    @Test
    fun retrieve_a_movie() = runBlockingTest {
        dao.insert(TestUtil.createEntityMovie(id = 4))
        val movie = dao.withId("4")
        assertNotNull(movie)
    }

    @Test
    fun retrieve_a_flow_of_movie() = runBlockingTest {
        dao.insert(
            TestUtil.createEntityMovie(id = 23, title = "other"),
            TestUtil.createEntityMovie(id = 24, title = "primary")
        )
        dao.distinctMovieFlow("24").apply {
            take(1).collect {
                assertNotNull(it)
                assertEquals(it?.id, "24")
                assertEquals(it?.title, "primary")
            }

            dao.withId("24")?.let { dao.update(it.copy(
                title = "updated",
                overview = "updatedOverview"
            )) }

            take(1).collect {
                assertNotNull(it)
                assertEquals(it?.id, "24")
                assertEquals(it?.title, "updated")
                assertEquals(it?.overview, "updatedOverview")
            }
        }
    }
}