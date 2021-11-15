package com.sunrisekcdeveloper.ui_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

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

class StCaptions @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.st_captions, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.StCaptions, 0, 0)
            resources.getText(typedArray.getResourceId(R.styleable.StCaptions_itemOne, R.string.na)).run {
                findViewById<TextView>(R.id.caption_item_one)?.text = this
            }
            typedArray.recycle()
        }
    }

    fun submitInformation(itemOne: String?, itemTwo: String?, itemThree: String?) {
        findViewById<TextView>(R.id.caption_item_one)?.apply {
            text = itemOne ?: "N/A"
        }
        findViewById<TextView>(R.id.caption_item_two)?.apply {
            text = itemTwo ?: "N/A"
        }
        findViewById<TextView>(R.id.caption_item_three)?.apply {
            text = itemThree ?: "N/A"
        }
    }
}