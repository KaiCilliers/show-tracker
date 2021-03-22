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

package com.sunrisekcdeveloper.showtracker.features.progress.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.features.progress.application.FetchShowSeasonAndEpisodeTotalsUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.progress.application.SetShowProgressUseCaseContract
import com.sunrisekcdeveloper.showtracker.features.watchlist.presentation.UIModelWatchlistShow
import kotlinx.coroutines.launch

class ViewModelProgress @ViewModelInject constructor(
    private val fetchShowSeasonAndEpisodeTotalsUseCase: FetchShowSeasonAndEpisodeTotalsUseCaseContract,
    private val setShowProgressUseCase: SetShowProgressUseCaseContract
) : ViewModel() {
    private val _showSeasonAndEpisodeCount = MutableLiveData<Resource<Map<Int, Int>>>()
    val showSeasonAndEpisodeCount: LiveData<Resource<Map<Int, Int>>>
        get() = _showSeasonAndEpisodeCount

    fun getShowSeasonAndEpisodeCount(showId: String) = viewModelScope.launch {
        val map = fetchShowSeasonAndEpisodeTotalsUseCase(showId)
        _showSeasonAndEpisodeCount.value = map
    }

    fun setShowProgress(
        showId: String,
        seasonNumber: Int,
        episodeNumber: Int
    ) = viewModelScope.launch {
        setShowProgressUseCase(SetShowProgress.Partial(showId, seasonNumber, episodeNumber))
    }

    fun setShowUpToDate(showId: String) = viewModelScope.launch {
        setShowProgressUseCase(SetShowProgress.UpToDate(showId))
    }
}

sealed class SetShowProgress {
    data class Partial (
        val showId: String,
        val seasonNumber: Int,
        val episodeNumber: Int
    ): SetShowProgress()
    data class UpToDate(val showId: String): SetShowProgress()
}