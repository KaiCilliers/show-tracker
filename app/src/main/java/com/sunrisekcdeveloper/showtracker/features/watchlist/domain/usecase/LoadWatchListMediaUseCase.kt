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

package com.sunrisekcdeveloper.showtracker.features.watchlist.domain.usecase

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.WatchlistRepo
import com.sunrisekcdeveloper.showtracker.features.watchlist.application.LoadWatchListMediaUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract

class LoadWatchListMediaUseCase(
    @WatchlistRepo private val watchListRepo: WatchListRepositoryContract
) : LoadWatchListMediaUseCaseContract {
    override suspend fun invoke(): Resource<List<MediaModelSealed>> {
        return watchListRepo.watchListMedia()
    }
}