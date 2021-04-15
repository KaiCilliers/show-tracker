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
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoWatchlistSeasonTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoWatchlistSeason

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.watchlistSeasonDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_watchlist_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1))
            val record = withId("1", 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.number, 1)
        }
    }

    @Test
    fun insert_multiple_watchlist_seasons() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistSeason(showId = 1, number = 1, currentEpisode = 10),
                TestUtil.createEntityWatchlistSeason(showId = 5, number = 3, currentEpisode = 1),
            )
            val recordOne = withId("1", 1)
            val recordTwo = withId("5", 3)

            assertNotNull(recordOne)
            assertEquals(recordOne?.showId, "1")
            assertEquals(recordOne?.number, 1)
            assertEquals(recordOne?.currentEpisode, 10)

            assertNotNull(recordTwo)
            assertEquals(recordTwo?.showId, "5")
            assertEquals(recordTwo?.number, 3)
            assertEquals(recordTwo?.currentEpisode, 1)
        }
    }

    @Test
    fun replace_watchlist_season_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1, currentEpisode = 10))
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1, currentEpisode = 8))
            val record = withId("1", 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.number, 1)
            assertEquals(record?.currentEpisode, 8)
        }
    }

    @Test
    fun update_existing_watchlist_season_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1, currentEpisode = 8))

            withId("1", 1).apply {
                this?.let {
                    update(it.copy(
                        dateStarted = 88L,
                        dateCompleted = 88L,
                        completed = true,
                        currentEpisode = 88,
                        startedTrackingSeason = true,
                        finishedBeforeTracking = true,
                        lastUpdated = 88L
                    ))
                }
            }

            val record = withId("1", 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.number, 1)
            assertEquals(record?.dateStarted, 88L)
            assertEquals(record?.dateCompleted, 88L)
            assertEquals(record?.completed, true)
            assertEquals(record?.currentEpisode, 88)
            assertEquals(record?.startedTrackingSeason, true)
            assertEquals(record?.finishedBeforeTracking, true)
            assertEquals(record?.lastUpdated, 88L)
        }
    }

    @Test
    fun delete_a_watchlist_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1))
            withId("1", 1)?.let { delete(it) }
            assertNull(withId("1", 1))
        }
    }

    @Test
    fun retrieve_a_watchlist_season() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistSeason(showId = 1, number = 1))
            assertNotNull(withId("1", 1))
        }
    }

}