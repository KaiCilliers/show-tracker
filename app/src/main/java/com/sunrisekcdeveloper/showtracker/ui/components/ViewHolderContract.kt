package com.sunrisekcdeveloper.showtracker.ui.components

interface ViewHolderContract<T>{
    fun bind(item: T)
}