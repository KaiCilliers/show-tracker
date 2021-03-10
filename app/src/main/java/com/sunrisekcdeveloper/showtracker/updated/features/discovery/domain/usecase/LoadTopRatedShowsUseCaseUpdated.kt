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

package com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.usecase

import com.sunrisekcdeveloper.showtracker.commons.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscRepo
import com.sunrisekcdeveloper.showtracker.di.RepositoryModule.DiscoveryRepo
import com.sunrisekcdeveloper.showtracker.features.discover.application.LoadTopRatedShowsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.discover.domain.repository.DiscoveryRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.model.MediaModelSealed
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.application.LoadTopRatedShowsUseCaseContractUpdated
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.model.DiscoveryUIModel
import com.sunrisekcdeveloper.showtracker.updated.features.discovery.domain.repository.DiscoveryRepositoryContractUpdated

class LoadTopRatedShowsUseCaseUpdated(
    @DiscRepo private val discoveryRepo: DiscoveryRepositoryContractUpdated
) : LoadTopRatedShowsUseCaseContractUpdated {
    override suspend fun invoke(page: Int): Resource<List<DiscoveryUIModel>> =
        discoveryRepo.topRatedShows(page)
}