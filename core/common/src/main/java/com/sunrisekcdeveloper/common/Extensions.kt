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

package com.sunrisekcdeveloper.common

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

// Logging Timber delegate
inline fun <reified T: Any> T.timber(tag: String? = null) = TimberLoggerProperty<T>(tag)

// ViewBinding Delegate Fragment
fun <T: ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

// ViewBinding delegate Activity
inline fun <T: ViewBinding> AppCompatActivity.viewBinding(crossinline bindInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) { bindInflater.invoke(layoutInflater) }

/**
 * Recyclerview
 * Remove the adapter after the view has been detached from window to prevent memory leaks
 * Use when the current fragment is going to be REPLACED (Not needed when using FragmentTransaction.add)
 */
fun <VH: RecyclerView.ViewHolder> RecyclerView.setNullableAdapter(adapter: RecyclerView.Adapter<VH>) {
    this.adapter = adapter
    this.clearReference()
}
internal fun RecyclerView.clearReference() {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) { }

        override fun onViewDetachedFromWindow(v: View?) {
            this@clearReference.adapter = null
        }
    })
}