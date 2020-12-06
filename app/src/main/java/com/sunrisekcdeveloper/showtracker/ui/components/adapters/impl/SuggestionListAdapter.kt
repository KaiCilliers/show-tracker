package com.sunrisekcdeveloper.showtracker.ui.components.adapters.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.ui.components.adapters.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ui.components.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.entities.domain.diff.FeaturedListDiff
import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl.SuggestionListViewHolder

class SuggestionListAdapter(
    private val clickAction: ClickActionContract
) : BaseListAdapter<FeaturedList, SuggestionListViewHolder>(FeaturedListDiff()) {

    private var data: List<FeaturedList> = listOf()

    override fun submit(list: List<FeaturedList>) {
        this.data = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionListViewHolder =
        SuggestionListViewHolder(
            RcItemFeaturedBinding.inflate(
                LayoutInflater.from(parent.context)
            ), clickAction
        )

    override fun onBindViewHolder(holder: SuggestionListViewHolder, position: Int) {
        holder.bind(getItem(position))

        val subLayout = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val subAdapter = SmallPosterAdapter(clickAction)
        val subRc = holder.nestedList()

        subRc.layoutManager = subLayout
        subRc.adapter = subAdapter
        subAdapter.submit(getItem(position).results)
    }
}