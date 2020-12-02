package com.sunrisekcdeveloper.showtracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.databinding.FragmentHomeBinding
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.DisplayMovie
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction
import com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat.FeaturedCategoryAdapter
import com.sunrisekcdeveloper.showtracker.util.click
import timber.log.Timber

class HomeFragment : Fragment() {

    private val adapter by lazy {
        FeaturedCategoryAdapter(
            PosterClickAction { title ->
                Timber.d("Featured: $title")
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.rcFeaturedCategories.adapter = adapter
        binding.rcFeaturedCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.addData(populateFeaturedDummy())
    }

    private fun populateFeaturedDummy(): List<FeaturedList> =
        listOf(
            FeaturedList(
                "Featured",
                listOf<DisplayMovie>(
                    DisplayMovie("ONE ONE"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<DisplayMovie>(
                    DisplayMovie("TWO TWO"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<DisplayMovie>(
                    DisplayMovie("THREE THREE"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<DisplayMovie>(
                    DisplayMovie("FOUR FOUR"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
                listOf<DisplayMovie>(
                    DisplayMovie("FIVE FIVE"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Horror",
                listOf<DisplayMovie>(
                    DisplayMovie("SIX SIX"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
                listOf<DisplayMovie>(
                    DisplayMovie("SEVEN SEVEN"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Featured",
                listOf<DisplayMovie>(
                    DisplayMovie("EIGHT EIGHT"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "New Releases",
                listOf<DisplayMovie>(
                    DisplayMovie("NINE NINE"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Comedy",
                listOf<DisplayMovie>(
                    DisplayMovie("TEN TEN"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Watchlist",
                listOf<DisplayMovie>(
                    DisplayMovie("ELEVEN ELEVEN"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "For you",
                listOf<DisplayMovie>(
                    DisplayMovie("Finding Nemo"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Horror",
                listOf<DisplayMovie>(
                    DisplayMovie("Finding Nemo"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            ),
            FeaturedList(
                "Beacuse you watched Breaking Bad",
                listOf<DisplayMovie>(
                    DisplayMovie("Finding Nemo"),
                    DisplayMovie("Harry Potter"),
                    DisplayMovie("Deadpool"),
                    DisplayMovie("Jurassic Park"),
                    DisplayMovie("Forest Gump"),
                    DisplayMovie("Mall Cop"),
                    DisplayMovie("Miss Congeniality"),
                    DisplayMovie("Gladiator"),
                    DisplayMovie("Finding Dory")
                )
            )
        )

}