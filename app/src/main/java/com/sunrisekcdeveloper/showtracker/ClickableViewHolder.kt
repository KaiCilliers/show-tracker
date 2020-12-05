package com.sunrisekcdeveloper.showtracker

import androidx.viewbinding.ViewBinding

abstract class ClickableViewHolder<T>(binding: ViewBinding, clickAction: ClickActionContract<T>) : BaseViewHolder<T>(binding)