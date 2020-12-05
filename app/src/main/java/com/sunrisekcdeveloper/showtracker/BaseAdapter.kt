package com.sunrisekcdeveloper.showtracker

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    AdapterContract<T>