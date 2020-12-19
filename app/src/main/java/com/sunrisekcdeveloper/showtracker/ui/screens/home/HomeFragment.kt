/*
 * Copyright © 2020. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.ui.screens.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.data.local.MovieDao
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.FragmentHomeBinding
import com.sunrisekcdeveloper.showtracker.model.FeaturedList
import com.sunrisekcdeveloper.showtracker.model.Movie
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl.SuggestionListAdapter
import com.sunrisekcdeveloper.showtracker.util.datastate.Resource
import com.sunrisekcdeveloper.showtracker.util.subscribe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

/**
 * Home Fragment displays suggested movies and shows for the user to add to their watchlist,
 * categorised with appropriate headings
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var adapter: SuggestionListAdapter

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private var trending: List<Movie> = listOf()
    private var popular: List<Movie> = listOf()
    private var boxoffice: List<Movie> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setupBinding()
        observeViewModel()
        Timber.e("BOOOOOBS")
        return binding.root
    }

    private fun setupBinding() {
        // Temporal Coupling
        adapter.addOnClickAction(object : ClickActionContract {
            override fun onClick(item: Any) {
                Timber.d("Featured: $item")
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentDestToDetailFragment("FROM HOME FRAGMENT")
                )
            }
        })
        binding.rcFeaturedCategories.adapter = adapter
        binding.rcFeaturedCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    private fun update() {
        adapter.submitList(
            listOf(
                FeaturedList("Trending", trending),
                FeaturedList("BoxOffice", boxoffice),
                FeaturedList("Popular", popular)
            )
        )
    }

    private fun observeViewModel() {
//        viewModel.featuredData.subscribe(viewLifecycleOwner) {
//            when (it) {
//                is Resource.Loading -> { Timber.d("Loading") }
//                is Resource.Error -> { Timber.d("Erorr") }
//                is Resource.Success -> {
//                    Timber.d("Success")
//                    adapter.submitList(it.data)
//                }
//            }
//        }
        viewModel.trend.subscribe(viewLifecycleOwner) {
            Timber.d("Trending movies: ${it.size}")
            trending = it
            update()
        }
        viewModel.box.subscribe(viewLifecycleOwner) {
            Timber.d("Box movies: ${it.size}")
            boxoffice = it
            update()
        }
        viewModel.pop.subscribe(viewLifecycleOwner) {
            Timber.d("Popular movies: ${it.size}")
            popular = it
            update()
        }
//        viewModel.box.subscribe(viewLifecycleOwner) {
//            Timber.d("$it")
//            adapter.submit(it)
//        }
//        viewModel.pop.subscribe(viewLifecycleOwner) {
//            Timber.d("$it")
//            adapter.submit(it)
//        }
//        viewModel.trend.subscribe(viewLifecycleOwner) {
//            Timber.d("$it")
//            adapter.submit(it)
//        }
//        viewModel.featuredListData.subscribe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
    }
}