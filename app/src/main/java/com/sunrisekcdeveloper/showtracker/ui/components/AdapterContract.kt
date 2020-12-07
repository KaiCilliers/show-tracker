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

package com.sunrisekcdeveloper.showtracker.ui.components

/**
 * Adapter Contract defines the responsibilities of adapters
 *
 * @param T is the data type required by the associated ViewHolder
 */
interface AdapterContract<in T> {
    /**
     * Submit defines how the adapter provides a list of data
     *
     * @param list is a list of the data type required by the associated ViewHolder
     */
    fun submit(list: List<T>)

    /**
     * Add on click action defines a click action that a ViewHolder might require, thus not all
     * adapter will require this method
     *
     * @param action is the click action to be triggered when a ViewHolder is clicked
     */
    fun addOnClickAction(action: ClickActionContract)
}