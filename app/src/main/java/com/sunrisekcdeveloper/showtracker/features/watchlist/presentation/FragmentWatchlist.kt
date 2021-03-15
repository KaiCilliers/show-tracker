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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.common.Resource
import com.sunrisekcdeveloper.showtracker.databinding.FragmentWatchlistBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentWatchlist : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding

    private val viewModel: ViewModelWatchlist by viewModels()

    @Inject
    lateinit var watchlistMovieAdapter: AdapterWatchlistMovie
    private lateinit var watchlistMovieLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.watchlistMovies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    watchlistMovieAdapter.refreshList(it.data)
                }
                is Resource.Error -> {}
                Resource.Loading -> {}
            }
        }
    }

    private fun binding() {
        watchlistMovieLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewWatchlist.layoutManager = watchlistMovieLayoutManager
        binding.recyclerviewWatchlist.adapter = watchlistMovieAdapter
    }
}