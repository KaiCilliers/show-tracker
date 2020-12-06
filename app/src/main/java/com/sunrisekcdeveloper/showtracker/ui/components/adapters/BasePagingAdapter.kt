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

package com.sunrisekcdeveloper.showtracker.ui.components.adapters

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunrisekcdeveloper.showtracker.ui.components.AdapterContract

/**
 * Base PagingAdapter represents an adapter that extends [PagingDataAdapter] and implements
 * [AdapterContract]
 *
 * @param T is the data type required by the associated [RecyclerView.ViewHolder]
 * @param VH is a class that extends [RecyclerView.ViewHolder]
 * @param diff is a class that extends [DiffUtil.ItemCallback]
 */
abstract class BasePagingAdapter<T: Any, VH: RecyclerView.ViewHolder>(
    diff: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, VH>(diff), AdapterContract<T>