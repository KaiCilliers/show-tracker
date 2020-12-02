package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class FeaturedCategoryAdapter(
    val clickListener: PosterClickAction
) : ListAdapter<FeaturedList, FeaturedCategoryViewHolder>(CategoryDifferenceCallBack()){

    // TODO better impl, mayhap make a call to subList's submistList in addData?
    // allows onBind access to data to populate sub recyclerview
    private var data: List<FeaturedList> = listOf()

    // TODO make this a requirement from an interface for all adapters
    fun addData(data: List<FeaturedList>) {
        this.data = data
        submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCategoryViewHolder =
        FeaturedCategoryViewHolder.from(parent)

    override fun onBindViewHolder(holder: FeaturedCategoryViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)

        val subLayout = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val subAdapter = FilterAdapter(clickListener)
        holder.view().rcFeaturedList.layoutManager = subLayout
        holder.view().rcFeaturedList.adapter = subAdapter
        subAdapter.addData(getItem(position).results)
    }
}