package com.sunrisekcdeveloper.showtracker

interface AdapterContract<in T> {
    fun submit(list: List<T>)
}