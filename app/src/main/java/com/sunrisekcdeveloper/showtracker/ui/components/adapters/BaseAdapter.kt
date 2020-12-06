package com.sunrisekcdeveloper.showtracker.ui.components.adapters

import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.AdapterContract

abstract class BaseAdapter<T, VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    AdapterContract<T>