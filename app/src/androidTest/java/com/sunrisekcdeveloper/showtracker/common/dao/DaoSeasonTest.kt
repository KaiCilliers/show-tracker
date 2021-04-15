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
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat as hamAssertThat
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.collection.IsEmptyCollection.empty


@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoSeasonTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoSeason

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.seasonDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntitySeason(showId = 1, seasonNumber = 1))
            val season = withId("1", 1)
            assertNotNull(season)
            assertEquals(season?.showId, "1")
            assertEquals(season?.number, 1)
        }
    }

    @Test
    fun insert_multiple_seasons() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntitySeason(showId = 1, id = 2, seasonNumber = 2),
                TestUtil.createEntitySeason(showId = 1, id = 4, seasonNumber = 5)
            )
            val seasonOne = withId("1", 2)
            val seasonTwo = withId("1", 5)

            assertNotNull(seasonOne)
            assertNotNull(seasonTwo)
            assertEquals(seasonOne?.showId, "1")
            assertEquals(seasonOne?.number, 2)
            assertEquals(seasonOne?.id, 2)
            assertEquals(seasonTwo?.showId, "1")
            assertEquals(seasonTwo?.number, 5)
            assertEquals(seasonTwo?.id, 4)
        }
    }

    @Test
    fun replace_season_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntitySeason(showId = 2, seasonNumber = 2, name = "a"))
            insert(TestUtil.createEntitySeason(showId = 2, seasonNumber = 6, name = "b"))
            insert(TestUtil.createEntitySeason(showId = 2, seasonNumber = 2, name = "c"))

            val season = withId("2", 2)

            assertNotNull(season)
            assertEquals(season?.showId, "2")
            assertEquals(season?.number, 2)
            assertEquals(season?.name, "c")
        }
    }

    @Test
    fun update_existing_season_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntitySeason(
                    showId = 1,
                    id = 1,
                    seasonNumber = 1,
                    name = "original"
                )
            )

            withId("1", 1)?.let {
                update(
                    it.copy(
                        name = "newName",
                        overview = "newOverview",
                        posterPath = "newPosterPath",
                        airDate = -88L,
                        episodeTotal = -88,
                        lastUpdated = -88L
                    )
                )
            }

            val season = withId("1", 1)

            assertNotNull(season)
            assertEquals(season?.showId, "1")
            assertEquals(season?.number, 1)
            assertEquals(season?.id, 1)
            assertEquals(season?.name, "newName")
            assertEquals(season?.overview, "newOverview")
            assertEquals(season?.posterPath, "newPosterPath")
            assertEquals(season?.airDate, -88L)
            assertEquals(season?.episodeTotal, -88)
            assertEquals(season?.lastUpdated, -88L)
        }
    }

    @Test
    fun delete_a_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntitySeason(showId = 1, seasonNumber = 1))
            withId("1", 1)?.let { delete(it) }
            assertNull(withId("1", 1))
        }
    }

    @Test
    fun retrieve_a_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntitySeason(showId = 1, seasonNumber = 1))
            assertNotNull(withId("1", 1))
        }
    }

    @Test
    fun retrieve_all_seasons_from_a_show() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntitySeason(showId = 1, id = 1, seasonNumber = 1),
                TestUtil.createEntitySeason(showId = 2, id = 1, seasonNumber = 1),
                TestUtil.createEntitySeason(showId = 1, id = 3, seasonNumber = 3),
                TestUtil.createEntitySeason(showId = 3, id = 1, seasonNumber = 1),
                TestUtil.createEntitySeason(showId = 4, id = 1, seasonNumber = 1),
                TestUtil.createEntitySeason(showId = 4, id = 2, seasonNumber = 2),
                TestUtil.createEntitySeason(showId = 4, id = 3, seasonNumber = 3),
                TestUtil.createEntitySeason(showId = 1, id = 4, seasonNumber = 4),
                TestUtil.createEntitySeason(showId = 1, id = 2, seasonNumber = 2)
            )

            val seasons = allFromShow("1")

            hamAssertThat(seasons, not(empty()))
            hamAssertThat(seasons, hasSize(4))
            hamAssertThat(
                seasons, contains(
                    TestUtil.createEntitySeason(showId = 1, id = 1, seasonNumber = 1),
                    TestUtil.createEntitySeason(showId = 1, id = 2, seasonNumber = 2),
                    TestUtil.createEntitySeason(showId = 1, id = 3, seasonNumber = 3),
                    TestUtil.createEntitySeason(showId = 1, id = 4, seasonNumber = 4)
                )
            )
        }
    }

    @Test
    fun retrieve_last_season_in_show() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntitySeason(showId = 1, id = 2, seasonNumber = 2),
                TestUtil.createEntitySeason(showId = 2, id = 1, seasonNumber = 3),
                TestUtil.createEntitySeason(showId = 1, id = 3, seasonNumber = 3),
                TestUtil.createEntitySeason(showId = 2, id = 2, seasonNumber = 4),
                TestUtil.createEntitySeason(showId = 1, id = 1, seasonNumber = 1)
            )

            val season = lastInShow("1")

            assertNotNull(season)
            assertEquals(season?.showId, "1")
            assertEquals(season?.id, 3)
            assertEquals(season?.number, 3)
        }
    }
}