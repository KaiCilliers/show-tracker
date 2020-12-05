package com.sunrisekcdeveloper.showtracker

interface ViewHolderContract<T>{
    fun bind(item: T)
}