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

package com.sunrisekcdeveloper.showtracker.ui.components.viewholders.impl

import com.sunrisekcdeveloper.showtracker.ui.components.viewholders.BaseViewHolder
import com.sunrisekcdeveloper.showtracker.databinding.RcItemProgressHeaderBinding

/**
 * Today, Tomorrow, Next Week etc.
 * headers for upcoming episode releases
 */
class HeaderViewHolder(
    private val binding: RcItemProgressHeaderBinding
    ) : BaseViewHolder<String>(binding) {
    override fun bind(item: String) {
        binding.content = item
    }
}