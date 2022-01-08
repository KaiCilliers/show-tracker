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
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sunrisekcdeveloper.common.timber
import com.sunrisekcdeveloper.ui_components.databinding.StCollapsableHeadingBinding

class StCollapsableHeading @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = StCollapsableHeadingBinding.inflate(LayoutInflater.from(context), this, false)
    private var expanded = true

    init {
        binding.collapsableHeadingText.text = "Watched"
        addView(binding.root)
        binding.collapsableHeadingContainer.setOnClickListener {
            if (expanded) {
                binding.collapsableHeadingArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_expand_less_24))
                binding.collapsableHeadingButtonTemp.visibility = GONE
                expanded = false
            } else {
                binding.collapsableHeadingArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_expand_more_24))
                binding.collapsableHeadingButtonTemp.visibility = VISIBLE
                expanded = true
            }
        }
    }

}
