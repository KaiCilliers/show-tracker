package com.sunrisekcdeveloper.showtracker.ui.components

interface AdapterContract<in T> {
    fun submit(list: List<T>)
}