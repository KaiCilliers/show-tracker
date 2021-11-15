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

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sunrisekcdeveloper.ui_components.databinding.StCollapsableTextBinding

// https://medium.com/@yuriyskul/expandable-textview-with-layouttransition-part-1-b506681e78e7
// https://www.androiddesignpatterns.com/2016/11/introduction-to-icon-animation-techniques.html
class StCollapsableText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = StCollapsableTextBinding.inflate(LayoutInflater.from(context), this, false)
    private var expanded = true
    private var short: CharSequence = ""
    private var long: CharSequence = ""

    init {
        addView(binding.root)
        LayoutTransition().run {
            setDuration(2000)
            enableTransitionType(LayoutTransition.CHANGING)
            binding.root.layoutTransition = this
        }
    }

    fun setTheText(text: String) {
        binding.collapsableTextContent.text = text
    }

    fun getIngo() {
        short = binding.collapsableTextContent.layout.text
        long = binding.collapsableTextContent.text
        expanded = short == long
        binding.collapsableTextContent.setOnClickListener {
            if (expanded) {
                expanded = false
                binding.collapsableTextContent.maxLines = 3
            } else {
                expanded = true
                binding.collapsableTextContent.maxLines = Int.MAX_VALUE
            }
        }
    }
}
