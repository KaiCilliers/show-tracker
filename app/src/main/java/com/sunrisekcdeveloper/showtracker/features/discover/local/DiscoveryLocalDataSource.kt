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

import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedEntity
import com.sunrisekcdeveloper.showtracker.features.discover.models.FeaturedMovies
import com.sunrisekcdeveloper.showtracker.models.local.core.MovieEntity

class DiscoveryLocalDataSource(
    private val dao: DiscoveryDao
) : DiscoveryLocalDataSourceContract {
    override suspend fun featuredMovies(): List<FeaturedMovies> = dao.groupedFeatured()

    override suspend fun insertMovie(vararg movie: MovieEntity) = dao.insertMovie(*movie)

    override suspend fun replaceAllFeaturedMovies(vararg movies: FeaturedEntity) = dao.updateFeatured(*movies)
}