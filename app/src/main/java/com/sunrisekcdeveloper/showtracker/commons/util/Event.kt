/*
 * Copyright © 2020. The Android Open Source Project
 *
 * @author Kai Cilliers
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sunrisekcdeveloper.showtracker.commons.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 * The advantage of this approach is that the user needs to specify the intention by
 * using getContentIfNotHandled() or peekContent(). This method models the events as
 * part of the state: they’re now simply a message that has been consumed or not.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}


/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}

// inline variation
inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}

    // usage
//class ListViewModel : ViewModel {
//    private val _navigateToDetails = MutableLiveData<Event<String>>()
//
//    val navigateToDetails : LiveData<Event<String>>
//        get() = _navigateToDetails
//
//
//    fun userClicksOnButton(itemId: String) {
//        _navigateToDetails.value = Event(itemId)  // Trigger the event by setting a new Event as a new value
//    }
//}

//myViewModel.navigateToDetails.observe(this, Observer {
//    it.getContentIfNotHandled()?.let { // Only proceed if the event has never been handled
//        startActivity(DetailsActivity...)
//    }
//})