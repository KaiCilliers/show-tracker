package com.sunrisekcdeveloper.showtracker

interface ClickActionContract<in T> {
    fun onClick(item: T)
}