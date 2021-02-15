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

package com.sunrisekcdeveloper.showtracker.features.watchlist.data.repository

import com.sunrisekcdeveloper.showtracker.di.NetworkModule.WatchlistClient
import com.sunrisekcdeveloper.showtracker.features.watchlist.domain.repository.WatchListRepositoryContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.local.WatchlistDao
import com.sunrisekcdeveloper.showtracker.features.watchlist.data.network.WatchlistDataSourceContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WatchlistRepository(
    private val local: WatchlistDao,
    @WatchlistClient private val remote: WatchlistDataSourceContract
) : WatchListRepositoryContract {
    private val ioScope = CoroutineScope(Job() + Dispatchers.IO)
    private val cpuScope = CoroutineScope(Job() + Dispatchers.Default)

    // TODO
    //  move to a base class - in a commons package or utils
    private fun update(block: suspend () -> Unit) {
        ioScope.launch {
            block.invoke()
        }
    }
}

