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

package com.sunrisekcdeveloper.ui_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.sunrisekcdeveloper.ui_components.databinding.StSubListBinding
import com.sunrisekcdeveloper.ui_components.databinding.SubListItemBinding

class StSubList @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = StSubListBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
    }

    fun setHeading(text: String) {
        binding.subListHeading.text = text
    }

    fun setData(data: List<String>, action: (View, String) -> Unit = {_,_ ->}) {
        data.forEach { message ->
            SubListItemBinding.inflate(LayoutInflater.from(context), this, false).apply {
                this.subListItem.text = message
                this.subListItem.setOnClickListener {
                    action(binding.root, message)
                }
                binding.subListContents.addView(this.root)
            }
        }
    }
}
