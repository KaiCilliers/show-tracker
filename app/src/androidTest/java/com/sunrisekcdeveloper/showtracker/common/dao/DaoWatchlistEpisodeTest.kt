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
class DaoWatchlistEpisodeTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: TrackerDatabase
    lateinit var dao: DaoWatchlistEpisode

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.watchlistEpisodeDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_watchlist_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1))
            val record = withId("1", 1, 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.seasonNumber, 1)
            assertEquals(record?.episodeNumber, 1)
        }
    }

    @Test
    fun insert_multiple_watchlist_episodes() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1),
                TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 2),
            )
            val recordOne = withId("1", 1, 1)
            val recordTwo = withId("1", 2, 1)

            assertNotNull(recordOne)
            assertEquals(recordOne?.showId, "1")
            assertEquals(recordOne?.seasonNumber, 1)
            assertEquals(recordOne?.episodeNumber, 1)

            assertNotNull(recordTwo)
            assertEquals(recordTwo?.showId, "1")
            assertEquals(recordTwo?.seasonNumber, 1)
            assertEquals(recordTwo?.episodeNumber, 2)
        }
    }

    @Test
    fun replace_watchlist_episode_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1, lastUpdated = 1L))
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1, lastUpdated = 2L))

            val record = withId("1", 1, 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.seasonNumber, 1)
            assertEquals(record?.episodeNumber, 1)
            assertEquals(record?.lastUpdated, 2L)
        }
    }

    @Test
    fun update_existing_watchlist_episode_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1))

            withId("1", 1, 1).apply {
                this?.let {
                    update(it.copy(
                        watched = true,
                        initialSetProgressBatch = true,
                        viaUpToDateAction = true,
                        dateWatched = 1L,
                        onEpisodeSinceDate = 1L,
                        lastUpdated = 1L
                    ))
                }
            }

            val record = withId("1", 1, 1)

            assertNotNull(record)
            assertEquals(record?.showId, "1")
            assertEquals(record?.seasonNumber, 1)
            assertEquals(record?.episodeNumber, 1)
            assertEquals(record?.watched, true)
            assertEquals(record?.initialSetProgressBatch, true)
            assertEquals(record?.viaUpToDateAction, true)
            assertEquals(record?.dateWatched, 1L)
            assertEquals(record?.onEpisodeSinceDate, 1L)
            assertEquals(record?.lastUpdated, 1L)
        }
    }

    @Test
    fun delete_watchlist_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1))
            withId("1", 1, 1)?.let { delete(it) }
            assertNull(withId("1", 1, 1))
        }
    }

    @Test
    fun retrieve_a_watchlist_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistEpisode(showId = 1, seasonNumber = 1, number = 1))
            assertNotNull(withId("1", 1, 1))
        }
    }
}