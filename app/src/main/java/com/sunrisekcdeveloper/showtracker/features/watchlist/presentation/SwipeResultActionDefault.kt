/*
 * Copyright © 2021. The Android Open Source Project
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

package com.sunrisekcdeveloper.showtracker.features.watchlist.presentation

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction
import timber.log.Timber

open class SwipeResultActionDefault : SwipeResultAction(
    RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT
)

class SwipeLeftResultAction internal constructor(
    adapter: AdapterWatchlistShow?,
    position: Int
) :
    SwipeResultActionDefault() {
    private var mAdapter: AdapterWatchlistShow?
    private val mPosition: Int
    override fun onPerformAction() {
        super.onPerformAction()
        Timber.d("action pinned performed on: ${mAdapter?.itemAtId(mPosition)}")
    }

    override fun onSlideAnimationEnd() {
        super.onSlideAnimationEnd()
        Timber.d("slide animation end pinned :)")
    }

    override fun onCleanUp() {
        super.onCleanUp()
        // clear the references
        mAdapter = null
    }

    init {
        mAdapter = adapter
        mPosition = position
    }
}

class SwipeRightResultAction internal constructor(
    adapter: AdapterWatchlistShow?,
    position: Int
) :
    SwipeResultActionDefault() {
    private var mAdapter: AdapterWatchlistShow?
    private val mPosition: Int
    override fun onPerformAction() {
        super.onPerformAction()
        Timber.d("action remove performed on: ${mAdapter?.itemAtId(mPosition)}")
        mAdapter?.let {
            it.removeItem(mPosition)
            it.notifyItemRemoved(mPosition)
        }
    }

    override fun onSlideAnimationEnd() {
        super.onSlideAnimationEnd()
        Timber.d("on slide animation end")
    }

    override fun onCleanUp() {
        super.onCleanUp()
        // clear the references
        mAdapter = null
    }

    init {
        mAdapter = adapter
        mPosition = position
    }
}

class UnpinResultAction internal constructor(
    adapter: AdapterWatchlistShow?,
    position: Int
) :
    SwipeResultActionDefault() {
    private var mAdapter: AdapterWatchlistShow?
    private val mPosition: Int
    override fun onPerformAction() {
        super.onPerformAction()
        Timber.d("action performed on: ${mAdapter?.itemAtId(mPosition)}")
    }

    override fun onCleanUp() {
        super.onCleanUp()
        // clear the references
        mAdapter = null
    }

    init {
        mAdapter = adapter
        mPosition = position
    }
}