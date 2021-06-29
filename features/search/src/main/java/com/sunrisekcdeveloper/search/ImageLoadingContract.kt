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

package com.sunrisekcdeveloper.search

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

interface ImageLoadingContract {
    fun load(url: String, view: ImageView)
}

class ImageLoadingStandardGlide(private val context: Context) : ImageLoadingContract {
    constructor(activity: Activity) : this(activity.baseContext)
    constructor(fragment: Fragment) : this(fragment.requireContext())
    constructor(fragmentActivity: FragmentActivity) : this(fragmentActivity.baseContext)
    constructor(view: View) : this(view.context)
    override fun load(url: String, view: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .error(R.drawable.error_poster)
            .transition(DrawableTransitionOptions.withCrossFade(100))
            .into(view)
    }
}