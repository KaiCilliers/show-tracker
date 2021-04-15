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
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.FilterMovies
import com.sunrisekcdeveloper.showtracker.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.hasSize
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
class DaoWatchlistMovieTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoWatchlistMovie

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.watchlistMovieDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_watchlist_movie() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1, lastUpdated = 88L))
            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
            assertEquals(record?.dateLastUpdated, 88L)
        }
    }

    @Test
    fun insert_multiple_watchlist_movies() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, lastUpdated = 88L),
                TestUtil.createEntityWatchlistMovie(id = 2, lastUpdated = 99L)
            )
            val recordOne = withId("1")
            val recordTwo = withId("2")

            assertNotNull(recordOne)
            assertEquals(recordOne?.id, "1")
            assertEquals(recordOne?.dateLastUpdated, 88L)
            assertNotNull(recordTwo)
            assertEquals(recordTwo?.id, "2")
            assertEquals(recordTwo?.dateLastUpdated, 99L)
        }
    }

    @Test
    fun replace_watchlist_movie_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1, lastUpdated = 88L))
            insert(TestUtil.createEntityWatchlistMovie(id = 1, lastUpdated = 100L))
            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
            assertEquals(record?.dateLastUpdated, 100L)
        }
    }

    @Test
    fun update_existing_watchlist_movie_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1, lastUpdated = 88L))

            withId("1").apply {
                this?.let {
                    update(it.copy(
                        watched = true,
                        dateAdded = 100L,
                        dateWatched = 100L,
                        deleted = true,
                        dateDeleted = 100L,
                        dateLastUpdated = 100L
                    ))
                }
            }

            val record = withId("1")

            assertNotNull(record)
            assertEquals(record?.id, "1")
            assertEquals(record?.watched, true)
            assertEquals(record?.dateAdded, 100L)
            assertEquals(record?.dateWatched, 100L)
            assertEquals(record?.deleted, true)
            assertEquals(record?.dateDeleted, 100L)
            assertEquals(record?.dateLastUpdated, 100L)
        }
    }

    @Test
    fun delete_a_watchlist_movie() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1))
            withId("1")?.let { delete(it) }
            assertNull(withId("1"))
        }
    }

    @Test
    fun retrieve_a_watchlist_movie() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1))
            assertNotNull(withId("1"))
        }
    }

    @Test
    fun retrieve_all_unwatched_movies() = runBlockingTest {
        db.movieDao().insert(
            TestUtil.createEntityMovie(id = 1),
            TestUtil.createEntityMovie(id = 2),
            TestUtil.createEntityMovie(id = 3),
            TestUtil.createEntityMovie(id = 4),
            TestUtil.createEntityMovie(id = 5),
            TestUtil.createEntityMovie(id = 6),
        )
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = false, deleted = true),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = true, deleted = false),
                TestUtil.createEntityWatchlistMovie(id = 3, watched = true, deleted = true),
                TestUtil.createEntityWatchlistMovie(id = 4, watched = false, deleted = false),
                TestUtil.createEntityWatchlistMovie(id = 5, watched = false, deleted = true),
                TestUtil.createEntityWatchlistMovie(id = 6, watched = true, deleted = false),
            )

            val unwatched = unwatched()

            hamAssertThat(unwatched, not(empty()))
            hamAssertThat(unwatched, hasSize(1))
            assertEquals(unwatched[0].details.id, "4")
        }
    }

    @Test
    fun check_if_movie_exists() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityWatchlistMovie(id = 1))
            assertEquals(exist("1"), true)
            assertEquals(exist("2"), false)
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_movies_sorted_by_title() = runBlockingTest {
        db.movieDao().apply {
            insert(
                TestUtil.createEntityMovie(id = 1, title = "bb"),
                TestUtil.createEntityMovie(id = 2, title = "cc"),
                TestUtil.createEntityMovie(id = 3, title = "aa"),
                TestUtil.createEntityMovie(id = 4, title = "cb"),
            )
        }
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1),
                TestUtil.createEntityWatchlistMovie(id = 2),
                TestUtil.createEntityWatchlistMovie(id = 3),
                TestUtil.createEntityWatchlistMovie(id = 4),
            )

            distinctWithDetailsFlow(FilterMovies.NoFilters).apply {
                take(1).collect { movies ->

                    hamAssertThat(movies, not(empty()))
                    hamAssertThat(movies, hasSize(4))
                    hamAssertThat(movies, contains(
                        TestUtil.createWatchlistMovieWithDetails(id = 3, title = "aa"),
                        TestUtil.createWatchlistMovieWithDetails(id = 1, title = "bb"),
                        TestUtil.createWatchlistMovieWithDetails(id = 4, title = "cb"),
                        TestUtil.createWatchlistMovieWithDetails(id = 2, title = "cc"),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_movies_with_no_filters() = runBlockingTest {
        db.movieDao().apply {
            insert(
                TestUtil.createEntityMovie(id = 1, title = "bb"),
                TestUtil.createEntityMovie(id = 2, title = "cc"),
                TestUtil.createEntityMovie(id = 3, title = "aa"),
                TestUtil.createEntityMovie(id = 4, title = "cb"),
            )
        }
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = true, dateAdded = System.currentTimeMillis()),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = false, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 3, watched = true, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 4, watched = false, dateAdded = System.currentTimeMillis()),
            )

            distinctWithDetailsFlow(FilterMovies.NoFilters).apply {
                take(1).collect { movies ->
                    hamAssertThat(movies, not(empty()))
                    hamAssertThat(movies, hasSize(4))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_movies_filtered_by_watched() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.movieDao().apply {
            insert(
                TestUtil.createEntityMovie(id = 1, title = "bb"),
                TestUtil.createEntityMovie(id = 2, title = "cc"),
                TestUtil.createEntityMovie(id = 3, title = "aa"),
                TestUtil.createEntityMovie(id = 4, title = "cb"),
            )
        }
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = true, dateAdded = today),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = false, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 3, watched = true, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 4, watched = false, dateAdded = today),
            )

            distinctWithDetailsFlow(FilterMovies.Watched).apply {
                take(1).collect { movies ->
                    hamAssertThat(movies, not(empty()))
                    hamAssertThat(movies, hasSize(2))
                    hamAssertThat(movies, containsInAnyOrder(
                        TestUtil.createWatchlistMovieWithDetails(id = 1, title = "bb", watched = true, dateAdded = today),
                        TestUtil.createWatchlistMovieWithDetails(id = 3, title = "aa", watched = true, dateAdded = 1L),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_movies_filtered_by_unwatched() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.movieDao().apply {
            insert(
                TestUtil.createEntityMovie(id = 1, title = "bb"),
                TestUtil.createEntityMovie(id = 2, title = "cc"),
                TestUtil.createEntityMovie(id = 3, title = "aa"),
                TestUtil.createEntityMovie(id = 4, title = "cb"),
            )
        }
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = true, dateAdded = today),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = false, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 3, watched = true, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 4, watched = false, dateAdded = today),
            )

            distinctWithDetailsFlow(FilterMovies.Unwatched).apply {
                take(1).collect { movies ->
                    hamAssertThat(movies, not(empty()))
                    hamAssertThat(movies, hasSize(2))
                    hamAssertThat(movies, containsInAnyOrder(
                        TestUtil.createWatchlistMovieWithDetails(id = 2, title = "cc", watched = false, dateAdded = 1L),
                        TestUtil.createWatchlistMovieWithDetails(id = 4, title = "cb", watched = false, dateAdded = today),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_detailed_movies_filtered_by_added_today() = runBlockingTest {
        val today = System.currentTimeMillis()
        db.movieDao().apply {
            insert(
                TestUtil.createEntityMovie(id = 1, title = "bb"),
                TestUtil.createEntityMovie(id = 2, title = "cc"),
                TestUtil.createEntityMovie(id = 3, title = "aa"),
                TestUtil.createEntityMovie(id = 4, title = "cb"),
            )
        }
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = true, dateAdded = today),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = false, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 3, watched = true, dateAdded = 1L),
                TestUtil.createEntityWatchlistMovie(id = 4, watched = false, dateAdded = today),
            )

            distinctWithDetailsFlow(FilterMovies.AddedToday).apply {
                take(1).collect { movies ->
                    hamAssertThat(movies, not(empty()))
                    hamAssertThat(movies, hasSize(2))
                    hamAssertThat(movies, containsInAnyOrder(
                        TestUtil.createWatchlistMovieWithDetails(id = 1, title = "bb", watched = true, dateAdded = today),
                        TestUtil.createWatchlistMovieWithDetails(id = 4, title = "cb", watched = false, dateAdded = today),
                    ))
                }
            }
        }
    }

    @Test
    fun retrieve_a_flow_of_watchlist_movies() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityWatchlistMovie(id = 1, watched = true),
                TestUtil.createEntityWatchlistMovie(id = 2, watched = false)
            )
            distinctWatchlistMovieFlow("2").apply {
                take(1).collect {
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.watched, false)
                }
                withId("2")?.let { update(it.copy(watched = true)) }
                take(1).collect{
                    assertNotNull(it)
                    assertEquals(it?.id, "2")
                    assertEquals(it?.watched, true)
                }
            }
        }
    }
}