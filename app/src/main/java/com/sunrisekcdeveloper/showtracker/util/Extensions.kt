package com.sunrisekcdeveloper.showtracker.util

import android.view.View

inline fun View.click(crossinline action: () -> Unit) = setOnClickListener { action() }