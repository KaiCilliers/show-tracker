package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunrisekcdeveloper.showtracker.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.ClickActionContract
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class FeaturedCategoryAdapter(
    private val clickAction: ClickActionContract
) : BaseListAdapter<FeaturedList, FeaturedCategoryViewHolder>(CategoryDifferenceCallBack()) {

    private var data: List<FeaturedList> = listOf()

    override fun submit(list: List<FeaturedList>) {
        this.data = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCategoryViewHolder =
        FeaturedCategoryViewHolder(
            RcItemFeaturedBinding.inflate(
                LayoutInflater.from(parent.context)
            ), clickAction
        )

    override fun onBindViewHolder(holder: FeaturedCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))

        val subLayout = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val subAdapter = FilterAdapter(clickAction)
        val subRc = holder.nestedList()

        subRc.layoutManager = subLayout
        subRc.adapter = subAdapter
        subAdapter.submit(getItem(position).results)
    }
}