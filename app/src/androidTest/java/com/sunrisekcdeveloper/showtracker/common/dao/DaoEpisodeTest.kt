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
import org.hamcrest.CoreMatchers.not
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
class DaoEpisodeTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: TrackerDatabase
    private lateinit var dao: DaoEpisode

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.episodeDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_a_single_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "title"))

            val episode = withId("1", 1, 1)

            assertNotNull(episode)
            assertEquals(episode?.showId, "1")
            assertEquals(episode?.seasonNumber, 1)
            assertEquals(episode?.number, 1)
            assertEquals(episode?.name, "title")
        }
    }

    @Test
    fun insert_multiple_episodes() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "one"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 2, name = "two")
            )

            val episodeOne = withId("1", 1, 1)
            val episodeTwo = withId("1", 1, 2)

            assertNotNull(episodeOne)
            assertEquals(episodeOne?.showId, "1")
            assertEquals(episodeOne?.seasonNumber, 1)
            assertEquals(episodeOne?.number, 1)
            assertEquals(episodeOne?.name, "one")

            assertNotNull(episodeTwo)
            assertEquals(episodeTwo?.showId, "1")
            assertEquals(episodeTwo?.seasonNumber, 1)
            assertEquals(episodeTwo?.number, 2)
            assertEquals(episodeTwo?.name, "two")
        }
    }

    @Test
    fun replace_episode_on_matching_ids() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "original"))
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "new"))

            val episode = withId("1", 1, 1)

            assertNotNull(episode)
            assertEquals(episode?.showId, "1")
            assertEquals(episode?.seasonNumber, 1)
            assertEquals(episode?.number, 1)
            assertEquals(episode?.name, "new")
        }
    }

    @Test
    fun update_existing_episode_all_fields_except_id() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "title"))

            withId("1", 1, 1).apply {
                this?.let {
                    update(it.copy(
                        name = "newName",
                        airDate = -88L,
                        overview = "newOverview",
                        stillPath = "newStillPath",
                        lastUpdated = -88L
                    ))
                }
            }

            val episode = withId("1", 1, 1)

            assertNotNull(episode)
            assertEquals(episode?.showId, "1")
            assertEquals(episode?.seasonNumber, 1)
            assertEquals(episode?.number, 1)
            assertEquals(episode?.name, "newName")
            assertEquals(episode?.airDate, -88L)
            assertEquals(episode?.overview, "newOverview")
            assertEquals(episode?.stillPath, "newStillPath")
            assertEquals(episode?.lastUpdated, -88L)
        }
    }

    @Test
    fun delete_an_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "title"))
            withId("1", 1, 1)?.let { delete(it) }
            assertNull(withId("1", 1, 1))
        }
    }

    @Test
    fun retrieve_an_episode() = runBlockingTest {
        dao.apply {
            insert(TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1))
            assertNotNull(withId("1", 1, 1))
        }
    }

    @Test
    fun retrieve_first_episode_in_a_season() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 3, name = "three"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "one"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 2, number = 8, name = "eight"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 2, name = "two"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 4, name = "four")
            )

            val firstFromOne = firstInSeason("1", 1)
            val firstFromTwo = firstInSeason("1", 2)

            assertNotNull(firstFromOne)
            assertEquals(firstFromOne?.showId, "1")
            assertEquals(firstFromOne?.seasonNumber, 1)
            assertEquals(firstFromOne?.number, 1)
            assertEquals(firstFromOne?.name, "one")

            assertNotNull(firstFromTwo)
            assertEquals(firstFromTwo?.showId, "1")
            assertEquals(firstFromTwo?.seasonNumber, 2)
            assertEquals(firstFromTwo?.number, 8)
            assertEquals(firstFromTwo?.name, "eight")
        }
    }

    @Test
    fun retrieve_all_episodes_from_a_season() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 3, name = "three"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 1, name = "one"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 2, number = 8, name = "eight"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 2, name = "two"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 4, name = "four"),
                TestUtil.createEntityEpisode(showId = 2, seasonNumber = 9, number = 10, name = "ten"),
                TestUtil.createEntityEpisode(showId = 2, seasonNumber = 9, number = 12, name = "twelve"),
                TestUtil.createEntityEpisode(showId = 2, seasonNumber = 9, number = 14, name = "fourteen"),
                TestUtil.createEntityEpisode(showId = 2, seasonNumber = 9, number = 16, name = "sixteen"),
                TestUtil.createEntityEpisode(showId = 2, seasonNumber = 9, number = 18, name = "eighteen")
            )

            val allFromOne = allInSeason("1", 1)
            val allFromTwo = allInSeason("1", 2)
            val allFromNine = allInSeason("2", 9)

            hamAssertThat(allFromOne, not(empty()))
            hamAssertThat(allFromOne, hasSize(4))

            hamAssertThat(allFromTwo, not(empty()))
            hamAssertThat(allFromTwo, hasSize(1))

            hamAssertThat(allFromNine, not(empty()))
            hamAssertThat(allFromNine, hasSize(5))
        }
    }

    @Test
    fun retrieve_last_episode_from_a_season() = runBlockingTest {
        dao.apply {
            insert(
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 5, name = "five"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 6, name = "six"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 2, number = 7, name = "sevenTwo"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 3, number = 8, name = "eight"),
                TestUtil.createEntityEpisode(showId = 1, seasonNumber = 1, number = 7, name = "sevenOne"),
            )

            val episode = lastInSeason("1", 1)

            assertNotNull(episode)
            assertEquals(episode?.showId, "1")
            assertEquals(episode?.seasonNumber, 1)
            assertEquals(episode?.number, 7)
            assertEquals(episode?.name, "sevenOne")
        }
    }
}