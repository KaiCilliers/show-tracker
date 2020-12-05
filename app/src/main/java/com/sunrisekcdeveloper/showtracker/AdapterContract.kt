package com.sunrisekcdeveloper.showtracker

interface AdapterContract<in T> {
    fun addData(list: List<T>)
}

interface RealAdapterContract<in T> {
    fun populate(list: List<T>)
}