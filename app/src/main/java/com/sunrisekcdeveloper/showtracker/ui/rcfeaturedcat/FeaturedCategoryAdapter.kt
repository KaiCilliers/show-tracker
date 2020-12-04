package com.sunrisekcdeveloper.showtracker.ui.rcfeaturedcat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.BaseListAdapter
import com.sunrisekcdeveloper.showtracker.databinding.RcItemFeaturedBinding
import com.sunrisekcdeveloper.showtracker.entities.domain.FeaturedList
import com.sunrisekcdeveloper.showtracker.ui.rc.FilterAdapter
import com.sunrisekcdeveloper.showtracker.ui.rc.PosterClickAction

class FeaturedCategoryAdapter(
    val clickListener: PosterClickAction
) : BaseListAdapter<FeaturedList, FeaturedCategoryViewHolder>(CategoryDifferenceCallBack()){

    // TODO better impl, mayhap make a call to subList's submistList in addData?
    // allows onBind access to data to populate sub recyclerview
    private var data: List<FeaturedList> = listOf()

    // TODO make this a requirement from an interface for all adapters
    override fun addData(list: List<FeaturedList>) {
        this.data = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCategoryViewHolder =
        FeaturedCategoryViewHolder(
            RcItemFeaturedBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

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