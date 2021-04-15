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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterShows
import com.sunrisekcdeveloper.showtracker.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat as hamAssertThat
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.hamcrest.collection.IsEmptyCollection.empty

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class DaoWatchlistShowTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoWatchlistShow

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.watchlistShowDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_watchlist_show() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1))
            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
        }
    }

    @Test
    fun insert_multiple_watchlist_shows() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1),
                TestUtil.createEntityWatchlistShow(id = 2),
            )
            val recordOne = withId("1")
            val recordTwo = withId("2")

            assertNotNull(recordOne)
            assertEquals(recordOne?.id, "1")

            assertNotNull(recordTwo)
            assertEquals(recordTwo?.id, "2")
        }
    }

    @Test
    fun replace_watchlist_show_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1, lastUpdated = 100L))
            insert(TestUtil.createEntityWatchlistShow(id = 1, lastUpdated = 101L))
            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
            assertEquals(record?.lastUpdated, 101L)
        }
    }

    @Test
    fun update_existing_watchlist_show_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1, lastUpdated = 88L))

            withId("1").apply {
                this?.let {
                    update(it.copy(
                        currentEpisodeNumber = 88,
                        currentEpisodeName = "episodeName",
                        currentSeasonNumber = 88,
                        currentSeasonEpisodeTotal = 88,
                        started = true,
                        upToDate = true,
                        deleted = true,
                        dateDeleted = 88L,
                        dateAdded = 88L,
                        lastUpdated = 100L
                    ))
                }
            }

            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
            assertEquals(record?.currentEpisodeNumber, 88)
            assertEquals(record?.currentEpisodeName, "episodeName")
            assertEquals(record?.currentSeasonNumber, 88)
            assertEquals(record?.currentSeasonEpisodeTotal, 88)
            assertEquals(record?.started, true)
            assertEquals(record?.upToDate, true)
            assertEquals(record?.deleted, true)
            assertEquals(record?.dateDeleted, 88L)
            assertEquals(record?.dateAdded, 88L)
            assertEquals(record?.lastUpdated, 100)
        }
    }

    @Test
    fun delete_a_watchlist_show() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1))
            withId("1")?.let { delete(it) }
            assertNull(withId("1"))
        }
    }

    @Test
    fun retrieve_a_watchlist_show() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1))
            assertNotNull(withId("1"))
        }
    }

    @Test
    fun check_if_show_exists() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistShow(id = 1))
            assertEquals(exist("1"), true)
            assertEquals(exist("2"), false)
        }
    }

    @Test
    fun retrieve_all_shows_not_stared_watching() = runBlockingTest {
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1),
            TestUtil.createEntityShow(id = 2),
            TestUtil.createEntityShow(id = 3),
            TestUtil.createEntityShow(id = 4),
            TestUtil.createEntityShow(id = 5),
            TestUtil.createEntityShow(id = 6),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = false, deleted = true),
                TestUtil.createEntityWatchlistShow(id = 2, started = true, deleted = false),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, deleted = true),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, deleted = false),
                TestUtil.createEntityWatchlistShow(id = 5, started = false, deleted = true),
                TestUtil.createEntityWatchlistShow(id = 6, started = true, deleted = false),
            )

            val notStarted = unwatched()

            hamAssertThat(notStarted, not(empty()))
            hamAssertThat(notStarted, hasSize(1))
            assertEquals(notStarted[0].details.id, "4")
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_sorted_by_title() = runBlockingTest {
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1),
                TestUtil.createEntityWatchlistShow(id = 2),
                TestUtil.createEntityWatchlistShow(id = 3),
                TestUtil.createEntityWatchlistShow(id = 4),
            )

            distinctWithDetailsFlow(FilterShows.NoFilters).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(4))
                    hamAssertThat(shows, contains(
                        TestUtil.createWatchlistShowWithDetails(id = 3, title = "aa"),
                        TestUtil.createWatchlistShowWithDetails(id = 1, title = "bb"),
                        TestUtil.createWatchlistShowWithDetails(id = 4, title = "cb"),
                        TestUtil.createWatchlistShowWithDetails(id = 2, title = "cc"),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_with_no_filters() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
            TestUtil.createEntityShow(id = 5, title = "zz"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 2, started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                TestUtil.createEntityWatchlistShow(id = 5, started = true, upToDate = true, dateAdded = 1L, lastUpdated = 1L),
            )

            distinctWithDetailsFlow(FilterShows.NoFilters).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(5))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_filtered_by_added_today() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
            TestUtil.createEntityShow(id = 5, title = "zz"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 2, started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                TestUtil.createEntityWatchlistShow(id = 5, started = true, upToDate = true, dateAdded = 1L, lastUpdated = 1L),
            )

            distinctWithDetailsFlow(FilterShows.AddedToday).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(2))
                    hamAssertThat(shows, containsInAnyOrder(
                        TestUtil.createWatchlistShowWithDetails(id = 1, title = "bb",started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                        TestUtil.createWatchlistShowWithDetails(id = 4, title = "cb", started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_filtered_by_watched_today() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
            TestUtil.createEntityShow(id = 5, title = "zz"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 2, started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                TestUtil.createEntityWatchlistShow(id = 5, started = true, upToDate = true, dateAdded = 1L, lastUpdated = 1L),
            )

            distinctWithDetailsFlow(FilterShows.WatchedToday).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(3))
                    hamAssertThat(shows, containsInAnyOrder(
                        TestUtil.createWatchlistShowWithDetails(id = 1, title = "bb", started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                        TestUtil.createWatchlistShowWithDetails(id = 2, title = "cc", started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                        TestUtil.createWatchlistShowWithDetails(id = 3, title = "aa", started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_filtered_by_started() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
            TestUtil.createEntityShow(id = 5, title = "zz"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 2, started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                TestUtil.createEntityWatchlistShow(id = 5, started = true, upToDate = true, dateAdded = 1L, lastUpdated = 1L),
            )

            distinctWithDetailsFlow(FilterShows.Started).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(2))
                    hamAssertThat(shows, containsInAnyOrder(
                        TestUtil.createWatchlistShowWithDetails(id = 1, title = "bb", started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                        TestUtil.createWatchlistShowWithDetails(id = 3, title = "aa", started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_shows_filtered_by_not_started() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.showDao().insert(
            TestUtil.createEntityShow(id = 1, title = "bb"),
            TestUtil.createEntityShow(id = 2, title = "cc"),
            TestUtil.createEntityShow(id = 3, title = "aa"),
            TestUtil.createEntityShow(id = 4, title = "cb"),
            TestUtil.createEntityShow(id = 5, title = "zz"),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, started = true, upToDate = false, dateAdded = today, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 2, started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 3, started = true, upToDate = false, dateAdded = 1L, lastUpdated = today),
                TestUtil.createEntityWatchlistShow(id = 4, started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                TestUtil.createEntityWatchlistShow(id = 5, started = true, upToDate = true, dateAdded = 1L, lastUpdated = 1L),
            )

            distinctWithDetailsFlow(FilterShows.NotStarted).apply {
                take(1).collect { shows ->
                    hamAssertThat(shows, not(empty()))
                    hamAssertThat(shows, hasSize(2))
                    hamAssertThat(shows, containsInAnyOrder(
                        TestUtil.createWatchlistShowWithDetails(id = 2, title = "cc", started = false, upToDate = true, dateAdded = 1L, lastUpdated = today),
                        TestUtil.createWatchlistShowWithDetails(id = 4, title = "cb", started = false, upToDate = false, dateAdded = today, lastUpdated = 1L),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_watchlist_shows() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistShow(id = 1, upToDate = true),
                TestUtil.createEntityWatchlistShow(id = 2, upToDate = false),
            )
            distinctWatchlistShowFlow("2").apply {
                take(1).collect {
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.upToDate, false)
                }
                withId("2")?.let { update(it.copy(upToDate = true)) }
                take(1).collect {
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.upToDate, true)
                }
            }
        }
    }
}