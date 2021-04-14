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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.sunrisekcdeveloper.showtracker.common.TrackerDatabase
import com.sunrisekcdeveloper.showtracker.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoShowTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoShow

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.showDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_show() = runBlockingTest {
        dao.insert(TestUtil.createEntityShow(id = 1, title = "show"))
        val show = dao.withId("1")
        assertNotNull(this)
        assertEquals(show?.id, "1")
        assertEquals(show?.title, "show")
    }

    @Test
    fun insert_multiple_shows() = runBlockingTest {
        dao.insert(
            TestUtil.createEntityShow(id = 1, title = "one"),
            TestUtil.createEntityShow(id = 2, title = "two")
        )
        val showOne = dao.withId("1")
        val showTwo = dao.withId("2")
        assertNotNull(showOne)
        assertNotNull(showTwo)
        assertEquals(showOne?.id, "1")
        assertEquals(showTwo?.id, "2")
        assertEquals(showOne?.title, "one")
        assertEquals(showTwo?.title, "two")
    }

    @Test
    fun replace_show_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityShow(id = 1, title = "one"))
            insert(TestUtil.createEntityShow(id = 1, title = "two"))

            val show = withId("1")

            assertNotNull(show)
            assertEquals(show?.id, "1")
            assertEquals(show?.title, "two")
        }
    }

    @Test
    fun update_existing_movie_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityShow(id = 1, title = "one"))
            withId("1").apply {
                this?.let {
                    dao.update(
                        copy(
                            title = "newTitle",
                            overview = "newOverview",
                            certification = "newCertification",
                            posterPath = "newPosterPath",
                            backdropPath = "newBackdropPath",
                            popularityValue = -88f,
                            firstAirDate = "newFirstAirDate",
                            rating = -88f,
                            episodeTotal = 99,
                            seasonTotal = 99,
                            lastUpdated = -88L
                        )
                    )
                }
            }

            val show = withId("1")

            assertNotNull(show)
            assertEquals(show?.title, "newTitle")
            assertEquals(show?.overview, "newOverview")
            assertEquals(show?.certification, "newCertification")
            assertEquals(show?.posterPath, "newPosterPath")
            assertEquals(show?.backdropPath, "newBackdropPath")
            assertEquals(show?.popularityValue, -88f)
            assertEquals(show?.firstAirDate, "newFirstAirDate")
            assertEquals(show?.rating, -88f)
            assertEquals(show?.episodeTotal, 99)
            assertEquals(show?.seasonTotal, 99)
            assertEquals(show?.lastUpdated, -88L)
        }
    }

    @Test
    fun delete_a_show() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityShow(id = 1))
            withId("1")?.let { delete(it) }

            val show = withId("1")

            assertNull(show)
        }
    }

    @Test
    fun retrieve_a_show() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityShow(id = 1))
            assertNotNull(withId("1"))
        }
    }

    @Test
    fun retrieve_a_flow_of_shows() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityShow(id = 1, title = "other"),
                TestUtil.createEntityShow(id = 2, title = "primary")
            )
            distinctShowFlow("2").apply {
                take(1).collect {
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.title, "primary")
                }
                withId("2")?.let { update(it.copy(
                    title = "updated",
                    overview = "updatedOverview"
                )) }
                take(1).collect {
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.title, "updated")
                    assertEquals(it?.overview, "updatedOverview")
                }
            }
        }
    }

}