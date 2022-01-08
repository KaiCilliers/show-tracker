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
import android.widget.Toast
import com.sunrisekcdeveloper.ui_components.databinding.StAdditionlActionsBinding

class StAdditionalActions @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = StAdditionlActionsBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)

        binding.additionalActionsMyList.setImage(R.drawable.ic_baseline_add_24)
        binding.additionalActionsMyList.setText("My List")
        binding.additionalActionsMyList.setOnClickAction { Toast.makeText(context, "my list", Toast.LENGTH_SHORT).show() }

        binding.additionalActionsLike.setImage(R.drawable.ic_baseline_favorite_36)
        binding.additionalActionsLike.setText("Like")
        binding.additionalActionsLike.setOnClickAction { Toast.makeText(context, "like", Toast.LENGTH_SHORT).show() }

        binding.additionalActionsShare.setImage(R.drawable.ic_baseline_share_24)
        binding.additionalActionsShare.setText("Share")
        binding.additionalActionsShare.setOnClickAction { Toast.makeText(context, "share", Toast.LENGTH_SHORT).show() }
    }

}