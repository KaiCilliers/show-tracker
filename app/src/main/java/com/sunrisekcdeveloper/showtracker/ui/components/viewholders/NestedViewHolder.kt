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

package com.sunrisekcdeveloper.showtracker.ui.components.viewholders

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.sunrisekcdeveloper.showtracker.ui.components.ViewHolderContract

/**
 * Nested ViewHolder represents a viewholder that contains a [RecyclerView] as one of it's child
 * elements in the associated item layout and extends the abstract [BaseViewHolder]
 *
 * @param T is the data type required by the associated item layout
 * @param binding is the auto generated binding object that represents the associated item layout
 */
abstract class NestedViewHolder<T>(binding: ViewBinding) : BaseViewHolder<T>(binding) {
    /**
     * Nested List simply returns the [RecyclerView] child item in the associated item layout
     *
     * @return [RecyclerView] contained in the associated item layout
     */
    abstract fun nestedList(): RecyclerView
}